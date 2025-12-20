# DiaryBook

## Description

A multi-user diary application built in Java using Swing. Users can register, login, and maintain personal diaries. The application saves user data and diary entries to files for persistence.

## Features

- User registration and login system
- Multiple user support
- Password-protected accounts
- Diary entry creation and management
- Data persistence using file serialization
- Remember me functionality
- User-friendly GUI interface

## Requirements

- Java Development Kit (JDK) 8 or higher

## Installation and Running

1. Ensure JDK is installed on your system.
2. Navigate to the project directory.
3. Compile the source code:
   ```
   javac -d bin src/com/diarybook/main/*.java
   ```
4. Run the application:
   ```
   java -cp bin com.diarybook.main.DiaryBook
   ```

## Usage

1. Launch the application
2. Register a new user or login with existing credentials
3. Create and manage your diary entries
4. Data is automatically saved when closing the application

## Project Structure

- `src/com/diarybook/main/DiaryBook.java`: Main application class
- `src/com/diarybook/main/UserEnterUI.java`: Login/registration interface
- `src/com/diarybook/main/DiaryBookUI.java`: Main diary interface
- `src/com/diarybook/main/User.java`: User data model
- `src/com/diarybook/main/Diary.java`: Diary entry model
- `Users/`: Directory for storing user data files
- `bin/`: Compiled class files

## Data Storage

User data and diary entries are stored in the `Users/` directory using Java object serialization.

## Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.