#!/usr/bin/env python3
"""
Fetch image_url values from Nookipedia (MediaWiki API, no key required) and
write them into the three seed JSON files.

Usage:
    python3 scripts/fetch-images.py

After running, review UNMATCHED entries printed at the end, patch manually if
needed, then run:
    ./scripts/sync-data.sh
"""

import json
import time
import urllib.request
import urllib.parse
from pathlib import Path

DATA_DIR = Path(__file__).parent.parent / "src/main/resources/data"
WIKI_API = "https://nookipedia.com/w/api.php"
# Seconds between requests — be polite to the public wiki
DELAY = 0.5


def fetch_wiki_image(title: str):
    params = urllib.parse.urlencode({
        "action": "query",
        "titles": title,
        "prop": "pageimages",
        "format": "json",
        "pithumbsize": 400,
        "redirects": 1,
    })
    req = urllib.request.Request(
        f"{WIKI_API}?{params}",
        headers={"User-Agent": "ac-api-image-fetch/1.0 (github.com/ac-api)"},
    )
    try:
        with urllib.request.urlopen(req, timeout=10) as resp:
            data = json.loads(resp.read())
        pages = data.get("query", {}).get("pages", {})
        for page in pages.values():
            thumb = page.get("thumbnail", {}).get("source")
            if thumb:
                return thumb
    except Exception as e:
        print(f"  ERROR fetching '{title}': {e}")
    return None


def try_titles(candidates: list[str]):
    """Try each candidate title in order, return first image found."""
    for title in candidates:
        img = fetch_wiki_image(title)
        time.sleep(DELAY)
        if img:
            return img
    return None


# ---------------------------------------------------------------------------
# Villagers
# Wiki page titles: just the villager name (e.g. "Ankha", "Tom Nook")
# ---------------------------------------------------------------------------
def villager_titles(name: str) -> list[str]:
    return [name]


# ---------------------------------------------------------------------------
# Critters
# Fish/bugs/sea creatures have disambiguation on Nookipedia, e.g.
# "Bitterling (fish)" or just "Bitterling". Try plain name first.
# ---------------------------------------------------------------------------
def critter_titles(name: str, critter_type: str) -> list[str]:
    type_label = {"fish": "fish", "bug": "bug", "sea_creature": "sea creature"}
    label = type_label.get(critter_type, "")
    candidates = [name]
    if label:
        candidates.append(f"{name} ({label})")
    return candidates


# ---------------------------------------------------------------------------
# Museum entries
# Fossils: individual part pages don't exist; use the species page title.
#   e.g. "T. rex skull" → try "T. rex skull" then fall back to "T. rex"
# Art: plain display name works (e.g. "Amazing painting", "Beautiful statue").
# Forgeries have no Nookipedia pages — will be UNMATCHED.
# ---------------------------------------------------------------------------
_FOSSIL_SPECIES = {
    "t-rex":         "T. rex",
    "triceratops":   "Triceratops",
    "stegosaurus":   "Stegosaurus",
    "brachiosaurus": "Brachiosaurus",
    "mammoth":       "Mammoth",
    "ammonite":      "Ammonite",
    "trilobite":     "Trilobite",
}

def museum_titles(entry: dict) -> list[str]:
    name = entry["name"]
    category = entry["category"]
    entry_id = entry["id"]
    if category == "art":
        # Forgeries have no page — return just the name, will be UNMATCHED
        return [name]
    # fossil: try exact name, then species fallback
    candidates = [name]
    species_key = entry_id.rsplit("-", 1)[0]  # "t-rex-skull" → "t-rex"
    species_page = _FOSSIL_SPECIES.get(species_key)
    if species_page and species_page != name:
        candidates.append(species_page)
    return candidates


# ---------------------------------------------------------------------------
# Items
# Items often live at their exact display name. Some have "(furniture)" etc.
# Try plain name first, then with category disambiguation.
# ---------------------------------------------------------------------------
def item_titles(name: str, category: str) -> list[str]:
    cat_label = {
        "furniture": "furniture",
        "clothing": "clothing",
        "tool": "tool",
        "diy_recipe": "DIY recipe",
        "material": "material",
    }
    candidates = [name]
    label = cat_label.get(category)
    if label:
        candidates.append(f"{name} ({label})")
    return candidates


# ---------------------------------------------------------------------------
# Generic fetch loop
# ---------------------------------------------------------------------------
def enrich(entries: list[dict], get_titles, label: str) -> tuple[list[dict], list[str]]:
    unmatched = []
    total = len(entries)
    for i, entry in enumerate(entries, 1):
        if entry.get("image_url"):
            print(f"[{i}/{total}] {entry['name']} — already has image, skipping")
            continue
        titles = get_titles(entry)
        print(f"[{i}/{total}] {entry['name']} — trying {titles} ...", end=" ", flush=True)
        url = try_titles(titles)
        if url:
            entry["image_url"] = url
            print(f"OK")
        else:
            unmatched.append(f"{label}: {entry['name']} (id={entry['id']})")
            print("NOT FOUND")
    return entries, unmatched


def main():
    all_unmatched = []

    # --- Villagers ---
    path = DATA_DIR / "villagers.json"
    data = json.loads(path.read_text())
    print(f"\n=== Villagers ({len(data)}) ===")
    data, u = enrich(data, lambda e: villager_titles(e["name"]), "villager")
    all_unmatched.extend(u)
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n")
    print(f"Written: {path}")

    # --- Critters ---
    path = DATA_DIR / "critters.json"
    data = json.loads(path.read_text())
    print(f"\n=== Critters ({len(data)}) ===")
    data, u = enrich(data, lambda e: critter_titles(e["name"], e["type"]), "critter")
    all_unmatched.extend(u)
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n")
    print(f"Written: {path}")

    # --- Items ---
    path = DATA_DIR / "items.json"
    data = json.loads(path.read_text())
    print(f"\n=== Items ({len(data)}) ===")
    data, u = enrich(data, lambda e: item_titles(e["name"], e["category"]), "item")
    all_unmatched.extend(u)
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n")
    print(f"Written: {path}")

    # --- Museum ---
    path = DATA_DIR / "museum.json"
    data = json.loads(path.read_text())
    print(f"\n=== Museum ({len(data)}) ===")
    data, u = enrich(data, lambda e: museum_titles(e), "museum")
    all_unmatched.extend(u)
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n")
    print(f"Written: {path}")

    # --- Summary ---
    print("\n" + "=" * 60)
    if all_unmatched:
        print(f"UNMATCHED ({len(all_unmatched)}) — add image_url manually:")
        for m in all_unmatched:
            print(f"  {m}")
    else:
        print("All entries matched.")
    print("\nNext steps:")
    print("  ./scripts/sync-data.sh   # sync to docs/data/")
    print("  ./gradlew test           # verify data integrity")


if __name__ == "__main__":
    main()
