param(
    [switch]$SkipMaven = $false
)

$ErrorActionPreference = "Stop"
$ProjectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location -LiteralPath $ProjectRoot

if (-not $SkipMaven) {
    Write-Host "=== Compilando proyecto ===" -ForegroundColor Cyan
    mvn clean package -q
    if (-not $?) {
        Write-Host "ERROR: Fallo la compilacion con Maven." -ForegroundColor Red
        exit 1
    }
    Write-Host "Compilacion exitosa." -ForegroundColor Green
}

$TargetClasses = Join-Path $ProjectRoot "target\classes"
$TargetLib     = Join-Path $ProjectRoot "target\lib"

if (-not (Test-Path $TargetClasses) -or -not (Test-Path $TargetLib)) {
    Write-Host "ERROR: Ejecuta primero 'mvn clean package' para compilar." -ForegroundColor Red
    exit 1
}

Write-Host "=== Iniciando Blackjack 21 ===" -ForegroundColor Cyan
Write-Host ""

java --module-path "$TargetLib" `
    --add-modules javafx.controls,javafx.media `
    -cp "$TargetClasses;$TargetLib\*" `
    Juego21App
