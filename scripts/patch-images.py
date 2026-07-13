#!/usr/bin/env python3
"""
Apply all known image URLs and name fixes to the seed JSON files.
Run once, then discard this script.
"""

import json
from pathlib import Path

DATA = Path(__file__).parent.parent / "src/main/resources/data"

# ---------------------------------------------------------------------------
# Villagers
# ---------------------------------------------------------------------------
VILLAGER_IMAGES = {
    "raymond": "https://dodo.ac/np/images/2/2a/Raymond_NH.png",
}

# ---------------------------------------------------------------------------
# Critters
# ---------------------------------------------------------------------------
CRITTER_IMAGES = {
    "pond-smelt":  "https://dodo.ac/np/images/f/fc/Pond_Smelt_NH.png",
    "stringfish":  "https://dodo.ac/np/images/thumb/b/bf/Stringfish_NH.png/400px-Stringfish_NH.png",
    "sturgeon":    "https://dodo.ac/np/images/thumb/1/1c/Sturgeon_NH.png/400px-Sturgeon_NH.png",
    "ladybug":     "https://dodo.ac/np/images/3/33/Ladybug_NH.png",
}

# ---------------------------------------------------------------------------
# Items — new/corrected image URLs keyed by item id
# Also includes fixes for tools that previously got player artwork instead
# of item icons.
# ---------------------------------------------------------------------------
ITEM_IMAGES = {
    # furniture
    "wooden-chair":    "https://dodo.ac/np/images/a/a3/Wooden_Chair_%28Light_Wood%29_NH_Icon.png",
    "wooden-table":    "https://dodo.ac/np/images/c/c5/Wooden_Table_%28Light_Wood_-_None%29_NH_Icon.png",
    "wooden-bookshelf":"https://dodo.ac/np/images/6/65/Wooden_Bookshelf_%28Light_Brown%29_NH_Icon.png",
    "antique-bed":     "https://dodo.ac/np/images/7/7a/Antique_Bed_%28Brown%29_NH_Icon.png",
    "iron-chair":      "https://dodo.ac/np/images/d/df/Ironwood_Chair_%28Birch%29_NH_Icon.png",
    "campfire":        "https://dodo.ac/np/images/a/a7/Campfire_NH_Icon.png",
    # diy recipes
    "wedding-arch":    "https://dodo.ac/np/images/f/f2/Wedding_Arch_%28Cute%29_NH_Icon.png",
    "wedding-bench":   "https://dodo.ac/np/images/4/41/Wedding_Bench_%28Cute%29_NH_Icon.png",
    "birthday-cake":   "https://dodo.ac/np/images/c/ce/Birthday_Cake_%28Whipped-Cream_Topping%29_NH_Icon.png",
    "log-bench":       "https://dodo.ac/np/images/3/33/Log_Bench_%28Dark_Wood%29_NH_Icon.png",
    "log-stool":       "https://dodo.ac/np/images/1/14/Log_Stool_%28Dark_Wood%29_NH_Icon.png",
    # tools — item icons (replacing bad player-photo URLs from first pass)
    "fishing-rod":     "https://dodo.ac/np/images/thumb/2/2f/Fishing_Rod_NH_Icon.png/44px-Fishing_Rod_NH_Icon.png",
    "flimsy-fishing-rod": "https://dodo.ac/np/images/a/a1/Flimsy_Fishing_Rod_NH_Icon.png",
    "net":             "https://dodo.ac/np/images/thumb/1/15/Net_NH_Icon.png/44px-Net_NH_Icon.png",
    "flimsy-net":      "https://dodo.ac/np/images/9/91/Flimsy_Net_NH_Icon.png",
    "shovel":          "https://dodo.ac/np/images/thumb/7/7d/Shovel_NH_Icon.png/44px-Shovel_NH_Icon.png",
    "flimsy-shovel":   "https://dodo.ac/np/images/e/ea/Flimsy_Shovel_NH_Icon.png",
    "axe":             "https://dodo.ac/np/images/thumb/a/a0/Axe_NH_Icon.png/44px-Axe_NH_Icon.png",
    "flimsy-axe":      "https://dodo.ac/np/images/7/76/Flimsy_Axe_NH_Icon.png",
    "stone-axe":       "https://dodo.ac/np/images/a/a7/Stone_Axe_NH_Icon.png",
    "watering-can":    "https://dodo.ac/np/images/thumb/0/0f/Watering_Can_NH_Icon.png/44px-Watering_Can_NH_Icon.png",
    "slingshot":       "https://dodo.ac/np/images/thumb/6/6c/Slingshot_NH_Icon.png/44px-Slingshot_NH_Icon.png",
    # clothing
    "sailor-hat":      "https://dodo.ac/np/images/f/f2/Sailor%27s_Hat_%28White%29_NH_Icon.png",
    "straw-hat":       "https://dodo.ac/np/images/4/45/Straw_Hat_%28Red%29_NH_Icon.png",
    "tuxedo":          "https://dodo.ac/np/images/c/c1/Tuxedo_Jacket_%28White%29_NH_Icon.png",
    # materials
    "wood":            "https://dodo.ac/np/images/a/a2/Wood_NH_Icon.png",
    "hardwood":        "https://dodo.ac/np/images/d/de/Hardwood_NH_Icon.png",
    "iron-nugget":     "https://dodo.ac/np/images/f/fd/Iron_Nugget_NH_Icon.png",
    "white-rose":      "https://dodo.ac/np/images/f/fd/White_Roses_NH_Icon.png",
    "red-rose":        "https://dodo.ac/np/images/7/74/Red_Roses_NH_Icon.png",
    "sugar":           "https://dodo.ac/np/images/2/25/Sugar_NH_Icon.png",
    # fix bad gold-nugget image (was a NL photo, not an NH icon)
    "gold-nugget":     "https://dodo.ac/np/images/thumb/5/56/Gold_Nugget_NH_Icon.png/44px-Gold_Nugget_NH_Icon.png",
}

# Name corrections (id -> new name string)
ITEM_NAME_FIXES = {
    "iron-chair": "Ironwood chair",
}


def patch(path, image_map, name_fixes=None):
    data = json.loads(path.read_text())
    changed = 0
    for entry in data:
        eid = entry["id"]
        if name_fixes and eid in name_fixes:
            entry["name"] = name_fixes[eid]
            changed += 1
        if eid in image_map:
            entry["image_url"] = image_map[eid]
            changed += 1
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n")
    print(f"{path.name}: {changed} changes")


patch(DATA / "villagers.json", VILLAGER_IMAGES)
patch(DATA / "critters.json",  CRITTER_IMAGES)
patch(DATA / "items.json",     ITEM_IMAGES, ITEM_NAME_FIXES)
print("Done.")
