# ac-api

[![CI](https://github.com/tonela10/ac-api/actions/workflows/ci.yml/badge.svg)](https://github.com/tonela10/ac-api/actions/workflows/ci.yml)

A public REST API serving Animal Crossing data across all mainline games. Written in Kotlin with Ktor. Data ships as curated JSON files in the repo — no database required.

📖 **[API Reference](https://tonela10.github.io/ac-api/)** — interactive Swagger UI docs
📦 **[Bulk data dumps](https://tonela10.github.io/ac-api/data/)** — static JSON files, no server needed

## Two ways to consume this project

**As a live REST API with query-parameter filtering:** clone the repo, run `./gradlew run`, hit `http://localhost:8080`. Full filter/pagination semantics as documented in the API reference above.

**As static bulk data dumps (currently deployed):** fetch [villagers.json](https://tonela10.github.io/ac-api/data/villagers.json), [critters.json](https://tonela10.github.io/ac-api/data/critters.json), [museum.json](https://tonela10.github.io/ac-api/data/museum.json), [items.json](https://tonela10.github.io/ac-api/data/items.json), or [games.json](https://tonela10.github.io/ac-api/data/games.json) directly from GitHub Pages. Each file is a bare JSON array of the resource. No filtering — clients do it in-memory. Zero-cost delivery, no CORS issues.

## Quickstart

```bash
./gradlew run
```

The server starts on `http://localhost:8080`. Verify:

```bash
curl http://localhost:8080/health
curl 'http://localhost:8080/api/v1/villagers?species=cat'
curl 'http://localhost:8080/api/v1/villagers/raymond'
curl 'http://localhost:8080/api/v1/critters?type=fish&month=6&hemisphere=northern'
curl 'http://localhost:8080/api/v1/items?category=diy_recipe&theme=wedding'
curl http://localhost:8080/api/v1/games
```

Run tests:

```bash
./gradlew test
```

Run in Docker:

```bash
docker build -t ac-api .
docker run --rm -p 8080:8080 ac-api
```

## API overview

Base URL: `/api/v1`

| Resource   | Endpoint                         |
|------------|----------------------------------|
| Villagers  | `GET /villagers`, `GET /villagers/{id}` |
| Critters   | `GET /critters`, `GET /critters/{id}`   |
| Museum     | `GET /museum`, `GET /museum/{id}`       |
| Items      | `GET /items`, `GET /items/{id}`         |
| Games      | `GET /games`                            |
| Health     | `GET /health`                           |

### List response

```json
{
  "data": [ ... ],
  "pagination": {
    "page": 1,
    "page_size": 50,
    "total": 8,
    "total_pages": 1,
    "next": null,
    "prev": null
  }
}
```

Default `page_size` is 50; max is 200.

### Error response

```json
{ "error": { "code": "not_found", "message": "...", "status": 404 } }
```

Codes: `not_found` (404), `invalid_parameter` (400), `internal_error` (500).

### Filters

- **Villagers:** `species`, `personality`, `gender`, `game`, `birthday_month`, `hobby`, `zodiac`
- **Critters:** `type` (`fish`/`bug`/`sea_creature`), `game`, `location`, `month` (1-12), `hemisphere` (`northern`/`southern`), `time_of_day`, `min_price`, `max_price`, `shadow_size`, `rarity`
- **Museum:** `category` (`fossil`/`art`), `game`
- **Items:** `category` (`furniture`/`clothing`/`tool`/`diy_recipe`/`material`/`other`), `game`, `theme`, `source`, `min_price`, `max_price`, `size`

## Contributing data

Data lives in `src/main/resources/data/*.json` and is human-edited. See [CONTRIBUTING.md](CONTRIBUTING.md).

## Attribution

The initial dataset is bootstrapped from community sources including [Nookipedia](https://nookipedia.com/) and the Animal Crossing Fandom wiki. Animal Crossing is © Nintendo.

## License

Code: MIT. Data: CC-BY-SA 3.0 (inherited from upstream wikis).
