$mvPath = 'C:\Program Files\Apache\Maven\apache-maven-3.9.16\bin'
$curr = $env:Path
if (-not ($curr -split ';' | Where-Object { $_ -eq $mvPath })) {
  [Environment]::SetEnvironmentVariable('Path', $curr + ';' + $mvPath, 'User')
  $env:Path = $curr + ';' + $mvPath
  Write-Host "Added $mvPath to user PATH"
} else {
  Write-Host "$mvPath already in PATH"
}

mvn -v
mvn -DskipTests package -e
