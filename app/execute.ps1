$AssignEntry = "jar -ufe build\libs\app.jar org.example.App"
$Command = "java -jar build\libs\app.jar --mvf execute.ps1 build.gradle.kts To docs lib"
Invoke-Expression ($AssignEntry + " && " + $Command )
