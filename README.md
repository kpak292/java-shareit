# java-shareit
Template repository for Shareit project.

## DataBase

```mermaid
erDiagram
users{
    bigint id PK
    varchar(255) name
    varchar(255) email UK
}

items{
    bigint id PK
    bigint user_id FK
    varchar(255) name
    varchar(255) description
    boolean available
}

users ||--o{ items: user_id

```