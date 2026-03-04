# CLAUDE.md

## Role

In this project, Claude assists with backend development by committing on a per-feature basis and creating a PR once all tasks are complete.

---

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
Always create a GitHub Issue **before** creating a branch. Use the issue number in the branch name.
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

## PLAN Mode Workflow

When running in PLAN mode, follow this flow:

```
1. Break down the work into a task list
2. Create a GitHub Issue using the appropriate template (feature / bug / task)
3. Create a branch using the issue number
4. Execute each task
5. On feature unit completion → commit & push
6. On all tasks completion → create PR
```

### Issue Creation
Read the template file from `.github/ISSUE_TEMPLATE/` and auto-fill all fields based on the work context.

| Work Type | Template File |
|-----------|---------------|
| New feature | `.github/ISSUE_TEMPLATE/feature.yml` |
| Bug fix | `.github/ISSUE_TEMPLATE/bug.yml` |
| Refactoring, config, etc. | `.github/ISSUE_TEMPLATE/task.yml` |

**Steps**
1. Read the corresponding `.yml` template file to understand required fields.
2. Auto-fill all fields based on the planned work.
3. Create the issue via GitHub CLI.

```bash
# Create issue with the appropriate template
gh issue create --template feature.yml
gh issue create --template bug.yml
gh issue create --template task.yml
```

### Example Flow
```
[PLAN]
- [ ] Create GitHub Issue (feature template)               → get issue number
- [ ] Create branch (feature/[issue-number]-[summary])
- [ ] Define domain entity and JPA repository              → commit on completion (feat)
- [ ] Implement service logic                              → commit on completion (feat)
- [ ] Implement controller and request/response DTO        → commit on completion (feat)
- [ ] Add Swagger specification                            → commit on completion (docs)
- [ ] Create PR after all tasks are complete
```

---

## Creating a PR

Once all tasks are complete, create a PR targeting the `develop` branch.

**Steps**
1. Read `.github/pull_request_template.md` to understand required sections.
2. Auto-fill all sections based on the work done.
3. Create the PR via GitHub CLI.

```bash
gh pr create \
  --base develop \
  --title "[type]: [short description of work]" \
  --body "$(cat .github/pull_request_template.md)"
```

> Fill in each section of the template with actual content before creating the PR.
> The PR title should reflect the type and summary of the primary commit.
