$AssignEntry = "jar -ufe build\libs\app.jar org.example.App"
$Command = "java -jar build\libs\app.jar --cpf filem.exe build.gradle.kts To docs lib"
Invoke-Expression ($AssignEntry + " && " + $Command)
