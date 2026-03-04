## Branch Strategy

| Branch | Description |
|--------|-------------|
| `main` | Production deployment branch |
| `develop` | Feature integration branch (stage deployment) |
| `[type]/issue-number-work-summary` | Feature development branch |

### Branch Naming Convention
```
[type]/[issue-number]-[work-summary]
```

**Examples**
```
feature/1-add-club-api
fix/42-fix-auth-token
docs/15-update-readme
```

### Creating a Branch
Always branch off from `develop` before starting work.
```bash
git checkout develop
git pull origin develop
git checkout -b feature/[issue-number]-[work-summary]
```

---

## Commit Convention

### Commit Message Format
```
[type]: [short description of work]
```

### Type Reference

| Type | Description |
|------|-------------|
| `feat` | Add a new feature |
| `fix` | Fix a bug |
| `docs` | Documentation changes |
| `design` | Design or CSS changes |
| `style` | Code style changes |
| `build` | Build environment changes |
| `chore` | Minor changes (rename/move files or folders) |
| `hotfix` | Fix a critical production server error |

### Language Rule
- The `type` is written in **English**.
- The `short description of work` is written in **Korean**.

### Commit Message Examples
```
feat: 클럽 목록 조회 API 구현
fix: 인증 토큰 만료 오류 수정
docs: 클럽 API 스웨거 명세 추가
chore: 불필요한 임포트 제거
```

---

## Commit Unit Guidelines

**Commit per feature unit.**

A feature unit is defined as:
- One API endpoint fully implemented
- One DB schema or migration completed
- One service logic completed
- Any independently functional unit of work

### Running a Commit
```bash
git add .
git commit -m "[type]: [short description of work]"
git push origin [current-branch-name]
```

### Rules
- Do not commit incomplete features.
- Do not bundle multiple features into a single commit.
- Remove unnecessary `console.log` statements, stale comments, and commented-out code before committing.

---
