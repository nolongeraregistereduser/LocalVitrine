LocalVitrine

## Docker Run

### Prerequisites
- Docker Desktop installed and running

### Start all services
From the project root:

```bash
docker compose up --build
```

Services:
- Frontend: [http://localhost:4200](http://localhost:4200)
- Backend API: [http://localhost:8080](http://localhost:8080)
- MySQL: `localhost:3306` (`root` / `root`)

### Optional AI key
Set `BLAZE_API_KEY` in your shell before starting compose:

```bash
export BLAZE_API_KEY=your_key_here
docker compose up --build
```

On Windows PowerShell:

```powershell
$env:BLAZE_API_KEY="your_key_here"
docker compose up --build
```
