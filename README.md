# File Manager
A simple java application to manage files in the terminal.
- Supports (create, delete, copy, move, read) operations with files.
- Its not OS dependent.

# Dependencies
- [Java 23](https://adoptium.net/es/temurin/releases?version=23&mode=filter&os=any&arch=any)
- [Gradle_8.12](https://gradle.org/releases/#8.12)

# Installation
1. Clone the repository
```sh
git clone https://github.com/AlfonsoG-dev/FileManager
```
2. Access the project
```sh
cd FileManager
```
3. Build the project with `gradle`.
```sh
gradlew build
```
4. To use the `.jar` file as executable modify the `execute.ps1` script.
> There is some problems with the `.jar` file creation, at the moment it couldn't load the main class correctly 
>- So i do it manually updating the `.jar` file entry main class.
```sh
pwsh app\execute.ps1
```
> Or you can just simply create a binary executable.
>- On windows you can use [launch4j](https://launch4j.sourceforge.net)
>- On linux:
```sh
echo "#!/usr/bin/java -jar" > binary
cat app/build/libs/app.jar >> binary
chmod +x binary
./binary
```
> You can place that `binary` into a path an access it like a `CLI` application.
>- Create a folder to store that `binary`.
```sh
mkdir -p ${HOME}$/bin
```
>- Move that binary to the folder
```sh
mv binary ~/bin
```
>- Now export the binary into path to access that file globally.
```sh
export PATH="${HOME}/bin:${PATH}"
```
>- You can verify the file is in path with: `command -v binary`

<p style="color:grey;text-align:center;font-size:1.2em">All the application command will be available using --h</p>

# Disclaimer
- This project is for educational purposes.
- It is not intended to create a fully functional program.
- Security issues are not taken into account.
