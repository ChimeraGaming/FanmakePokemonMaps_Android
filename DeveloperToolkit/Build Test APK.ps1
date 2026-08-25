[CmdletBinding()]
param(
    [string]$GameSourcePath = "",
    [switch]$ConfigureOnly,
    [string]$GradleUserHomePath = ""
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest

$toolkitRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$gameSource = if ([string]::IsNullOrWhiteSpace($GameSourcePath)) {
    Join-Path $toolkitRoot "GameSource"
} else {
    [System.IO.Path]::GetFullPath($GameSourcePath)
}
$localPropertiesPath = Join-Path $toolkitRoot "devkit.local.properties"
$generatedConfigPath = Join-Path $toolkitRoot "app\src\main\assets\devkit.properties"
$outputFolder = Join-Path $toolkitRoot "Output"
$env:GRADLE_USER_HOME = if ([string]::IsNullOrWhiteSpace($GradleUserHomePath)) {
    Join-Path $toolkitRoot ".gradle-user"
} else {
    [System.IO.Path]::GetFullPath($GradleUserHomePath)
}
$env:ANDROID_USER_HOME = Join-Path $toolkitRoot ".android-user"
New-Item -ItemType Directory -Path $env:GRADLE_USER_HOME -Force | Out-Null
New-Item -ItemType Directory -Path $env:ANDROID_USER_HOME -Force | Out-Null
$buildHome = Join-Path $toolkitRoot ".build-home"
New-Item -ItemType Directory -Path $buildHome -Force | Out-Null

$localSdkFile = Join-Path $toolkitRoot "local.properties"
if (-not (Test-Path -LiteralPath $localSdkFile -PathType Leaf)) {
    $sdkCandidates = [System.Collections.Generic.List[string]]::new()
    if (-not [string]::IsNullOrWhiteSpace($env:ANDROID_HOME)) { $sdkCandidates.Add($env:ANDROID_HOME) }
    if (-not [string]::IsNullOrWhiteSpace($env:ANDROID_SDK_ROOT)) { $sdkCandidates.Add($env:ANDROID_SDK_ROOT) }
    if (-not [string]::IsNullOrWhiteSpace($env:LOCALAPPDATA)) {
        $sdkCandidates.Add((Join-Path $env:LOCALAPPDATA "Android\Sdk"))
    }
    $androidSdk = $sdkCandidates | Where-Object { Test-Path -LiteralPath $_ -PathType Container } | Select-Object -First 1
    if ($null -eq $androidSdk) {
        throw "Android SDK was not found. Install Android Studio and SDK 35, then run the build again."
    }
    $escapedSdk = $androidSdk.Replace("\", "\\").Replace(":", "\:")
    [System.IO.File]::WriteAllText($localSdkFile, "sdk.dir=$escapedSdk`n", [System.Text.UTF8Encoding]::new($false))
}

function Read-SimpleProperties {
    param([string]$Path)
    $values = @{}
    if (-not (Test-Path -LiteralPath $Path -PathType Leaf)) { return $values }
    foreach ($line in Get-Content -LiteralPath $Path) {
        $trimmed = $line.Trim()
        if ($trimmed.Length -eq 0 -or $trimmed.StartsWith("#")) { continue }
        $separator = $trimmed.IndexOf("=")
        if ($separator -le 0) { continue }
        $key = $trimmed.Substring(0, $separator).Trim()
        $value = $trimmed.Substring($separator + 1).Trim()
        $values[$key] = $value
    }
    return $values
}

function Test-GameRoot {
    param([System.IO.DirectoryInfo]$Folder)
    $hasEngineData = (Test-Path -LiteralPath (Join-Path $Folder.FullName "Data") -PathType Container) -or
        (Test-Path -LiteralPath (Join-Path $Folder.FullName "www") -PathType Container)
    if (-not $hasEngineData) { return $false }
    $hasIdentity = (Test-Path -LiteralPath (Join-Path $Folder.FullName "Game.ini") -PathType Leaf) -or
        ((Get-ChildItem -LiteralPath $Folder.FullName -File -Filter "*.exe" -ErrorAction SilentlyContinue).Count -gt 0)
    return $hasIdentity
}

function Find-Tracker {
    param([System.IO.DirectoryInfo]$Folder)
    $mvTracker = Join-Path $Folder.FullName "www\ZMapTracker.txt"
    if (Test-Path -LiteralPath $mvTracker -PathType Leaf) { return $mvTracker }
    $rootTracker = Join-Path $Folder.FullName "ZMapTracker.txt"
    if (Test-Path -LiteralPath $rootTracker -PathType Leaf) { return $rootTracker }
    return $null
}

if (-not (Test-Path -LiteralPath $gameSource -PathType Container)) {
    New-Item -ItemType Directory -Path $gameSource | Out-Null
}

$candidates = [System.Collections.Generic.List[System.IO.DirectoryInfo]]::new()
$sourceInfo = Get-Item -LiteralPath $gameSource
$candidates.Add($sourceInfo)
$levelOne = Get-ChildItem -LiteralPath $gameSource -Directory -ErrorAction SilentlyContinue
foreach ($folder in $levelOne) {
    $candidates.Add($folder)
    foreach ($child in Get-ChildItem -LiteralPath $folder.FullName -Directory -ErrorAction SilentlyContinue) {
        $candidates.Add($child)
    }
}

$gameRoots = @($candidates | Where-Object { Test-GameRoot $_ } | Sort-Object FullName -Unique)
if ($gameRoots.Count -eq 0) {
    throw "No RPG Maker game root was found under GameSource. Add one extracted game using a direct or nested layout."
}

$integratedRoots = @($gameRoots | Where-Object { $null -ne (Find-Tracker $_) })
if ($integratedRoots.Count -eq 0) {
    throw "A game was found, but it does not contain ZMapTracker.txt. Complete the tracker integration first."
}
if ($integratedRoots.Count -gt 1) {
    $paths = $integratedRoots.FullName -join "`n"
    throw "More than one integrated game was found. Keep only one game under GameSource.`n$paths"
}

$gameRoot = $integratedRoots[0]
$trackerPath = Find-Tracker $gameRoot
$tracker = Read-SimpleProperties $trackerPath
if ($tracker["tracker_format"] -ne "1") {
    throw "The tracker format must be 1."
}
$gameId = $tracker["game_id"]
if ([string]::IsNullOrWhiteSpace($gameId)) {
    throw "The tracker does not contain a game_id."
}

$local = Read-SimpleProperties $localPropertiesPath
$mapFolderName = $local["map_folder"]
if ([string]::IsNullOrWhiteSpace($mapFolderName)) {
    $mapFolders = @(Get-ChildItem -LiteralPath $gameRoot.FullName -Directory | Where-Object {
        $null -ne (Get-ChildItem -LiteralPath $_.FullName -File -ErrorAction SilentlyContinue | Where-Object {
            $_.Name -match '^Map\d{3,}\.(png|webp)$'
        } | Select-Object -First 1)
    })
    if ($mapFolders.Count -eq 0) {
        throw "No map folder containing Map001.png style images was found in the game root."
    }
    if ($mapFolders.Count -gt 1) {
        $names = $mapFolders.Name -join ", "
        throw "More than one map folder was found: $names. Set map_folder in devkit.local.properties."
    }
    $mapFolderName = $mapFolders[0].Name
}

$mapFolderPath = Join-Path $gameRoot.FullName $mapFolderName
if (-not (Test-Path -LiteralPath $mapFolderPath -PathType Container)) {
    throw "The configured map folder does not exist: $mapFolderName"
}

$displayName = $local["display_name"]
if ([string]::IsNullOrWhiteSpace($displayName)) {
    $gameIni = Join-Path $gameRoot.FullName "Game.ini"
    if (Test-Path -LiteralPath $gameIni -PathType Leaf) {
        $titleLine = Get-Content -LiteralPath $gameIni | Where-Object { $_ -match '^\s*Title\s*=' } | Select-Object -First 1
        if ($null -ne $titleLine) { $displayName = ($titleLine -split '=', 2)[1].Trim() }
    }
}
if ([string]::IsNullOrWhiteSpace($displayName)) { $displayName = $gameRoot.Name }

$safeDisplayName = $displayName.Replace("`r", " ").Replace("`n", " ")
$safeGameId = $gameId.Replace("`r", "").Replace("`n", "")
$safeMapFolder = $mapFolderName.Replace("`r", "").Replace("`n", "")
$config = @(
    "display_name=$safeDisplayName"
    "game_id=$safeGameId"
    "map_folder=$safeMapFolder"
) -join "`n"
[System.IO.Directory]::CreateDirectory((Split-Path -Parent $generatedConfigPath)) | Out-Null
[System.IO.File]::WriteAllText($generatedConfigPath, $config + "`n", [System.Text.UTF8Encoding]::new($false))

Write-Host "Detected game: $safeDisplayName"
Write-Host "Game root: $($gameRoot.FullName)"
Write-Host "Game ID: $safeGameId"
Write-Host "Map folder: $safeMapFolder"

if ($ConfigureOnly) {
    Write-Host "Configuration complete. Gradle build was skipped."
    return
}

$gradle = Join-Path $toolkitRoot "gradlew.bat"
if (-not (Test-Path -LiteralPath $gradle -PathType Leaf)) {
    throw "gradlew.bat is missing from the toolkit."
}

Push-Location $toolkitRoot
try {
    & $gradle ":app:assembleDebug" "--no-daemon" "--no-problems-report" `
        "-Duser.home=$buildHome" "-Pkotlin.compiler.execution.strategy=in-process"
    if ($LASTEXITCODE -ne 0) { throw "Gradle build failed with exit code $LASTEXITCODE." }
} finally {
    Pop-Location
}

$builtApk = Join-Path $toolkitRoot "app\build\outputs\apk\debug\app-debug.apk"
if (-not (Test-Path -LiteralPath $builtApk -PathType Leaf)) {
    throw "Gradle completed but app-debug.apk was not found."
}
New-Item -ItemType Directory -Path $outputFolder -Force | Out-Null
$outputApk = Join-Path $outputFolder "MapDevKitTest.apk"
Copy-Item -LiteralPath $builtApk -Destination $outputApk -Force
Write-Host "APK: $outputApk"
