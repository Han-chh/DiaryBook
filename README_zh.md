# DiaryBook

## 描述

使用Swing构建的多用户日记应用程序。用户可以注册、登录并维护个人日记。应用程序将用户数据和日记条目保存到文件中以实现持久性。

## 功能

- 用户注册和登录系统
- 多用户支持
- 密码保护账户
- 日记条目创建和管理
- 使用文件序列化的数据持久性
- 记住我功能
- 用户友好的GUI界面

## 要求

- Java开发工具包（JDK）8或更高版本

## 运行

```bash
java -jar DiaryBook.jar
```

## 使用方法

1. 启动应用程序
2. 注册新用户或使用现有凭据登录
3. 创建和管理您的日记条目
4. 关闭应用程序时数据会自动保存

## 项目结构

- `src/com/diarybook/main/DiaryBook.java`：主应用程序类
- `src/com/diarybook/main/UserEnterUI.java`：登录/注册界面
- `src/com/diarybook/main/DiaryBookUI.java`：主日记界面
- `src/com/diarybook/main/User.java`：用户数据模型
- `src/com/diarybook/main/Diary.java`：日记条目模型
- `Users/`：存储用户数据文件的目录
- `bin/`：编译的类文件

## 数据存储

用户数据和日记条目使用Java对象序列化存储在`Users/`目录中。

## 贡献

欢迎贡献！请随时提交问题和拉取请求。