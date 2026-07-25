# DiaryBook

A local Java desktop diary with user accounts, structured entry metadata, editing, pagination, and per-user persistence.

[中文说明](README_zh.md)

## Overview

DiaryBook provides a focused personal writing workflow: register or sign in, create a dated entry, attach weather, mood, location, and theme information, then return to saved entries for review or editing.

## Screenshot

![A completed synthetic diary entry with weather, mood, location, title, and content](assets/screenshots/diarybook-entry.jpg)

The screenshot comes from the running desktop application and uses disposable synthetic content.

## Features

- Local registration and login
- Date, weather, mood, location, theme, and content fields
- Entry creation, editing, deletion, and pagination
- Local export
- Serialized per-user storage

## Architecture

Multiple Swing windows coordinate authentication, entry composition, and diary browsing. User and diary objects are serialized to local files.

## Run

The committed artifact was verified with Java 25:

```bash
java -jar DiaryBook.jar
```

## Privacy notes

Diary data and remembered-user state are local files. Do not commit real diary content, credentials, `LASTUSER`, or personal `Users` data.
