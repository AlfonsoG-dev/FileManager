$AssignEntry = "jar -ufe build\libs\app.jar org.example.App"
$Command = "java -jar build\libs\app.jar --mvd bin build\classes To docs --r"
Invoke-Expression ($AssignEntry + " && " + $Command)
