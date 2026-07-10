# Contributing to ac-api

Thanks for helping! The API's value comes almost entirely from the quality of its data. Most contributions will be data fixes/additions rather than code.

## Contributing data

All data lives in `src/main/resources/data/*.json`. Each file is a plain JSON array of records.

1. Fork the repo and create a branch.
2. Edit the relevant JSON file. Keep entries sorted alphabetically by `id` where practical.
3. Use kebab-case `id` values (`raymond`, `emperor-butterfly`, `wedding-arch`).
4. Reference game codes from `games.json` (`acgc`, `ww`, `cf`, `nl`, `nh`, `pg`).
5. Run `./gradlew test` — the data integrity tests will catch unknown material references, duplicate ids, and missing required fields.
6. Open a PR. Cite your source (Nookipedia page URL, in-game screenshot, etc.) in the description.

### Adding a new resource type

If you want to add something the API doesn't cover yet (e.g. reactions, K.K. songs), open an issue first so we can agree on the schema before you write code.

## Contributing code

- Kotlin, Ktor, kotlinx.serialization.
- JVM 21, Gradle 9.
- Run `./gradlew build` before submitting a PR.
- Match existing formatting — the code style is Kotlin official.

## Reporting data errors

Open an issue with:
- The endpoint and id (e.g. `/api/v1/villagers/raymond`)
- What's wrong
- A source for the correct value