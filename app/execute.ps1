$AssignEntry = "jar -ufe build\libs\app.jar org.example.App"
$Command = "java -jar build\libs\app.jar --cpd bin build\classes To docs lib --r"
Invoke-Expression ($AssignEntry + " && " + $Command)
