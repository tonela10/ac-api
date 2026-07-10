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

### Adding images

Every resource has an optional `image_url` field. We don't host images — we hotlink to Nookipedia's CDN under their [permitted-use terms](https://nookipedia.com/wiki/Nookipedia:Copyrights). To add an image URL for an entry:

1. Find the entry's page on [nookipedia.com](https://nookipedia.com/) (e.g. Raymond's villager page).
2. Right-click the main image → "Copy image address". The URL should be under `https://dodo.ac/` or `https://static.wikia.nocookie.net/`.
3. Prefer transparent-background PNGs when both a PNG and JPG are available.
4. Verify the URL actually loads (paste in a browser) before committing.
5. Attribute Nookipedia in your PR description.

**Do not** commit guessed or auto-generated URLs — a broken image is worse than no image.

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