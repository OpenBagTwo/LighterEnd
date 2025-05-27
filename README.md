# LighterEnd

![server + client mod](https://img.shields.io/badge/Server\/Client-both-purple)
![mod loader: fabric/quilt](https://img.shields.io/badge/Mod_Loader-fabric%2Fquilt-a4cc37)
[![build status](https://github.com/OpenBagTwo/LighterEnd/actions/workflows/build.yml/badge.svg)](https://github.com/OpenBagTwo/LighterEnd/actions/workflows/build.yml)
![supported versions](https://img.shields.io/badge/Supported_Versions-1.21.6-blue)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/lighterend)](https://modrinth.com/mod/lighterend)

***A reimagined vision of a BetterEnd***

![logo](_static/logo_big.png)

## What Is This, and Why Does it Exist?

[Paulevs](https://github.com/paulevsGitch)' legendary mod
[BetterEnd](https://github.com/quiqueck/BetterEnd) drastically transformed Minecraft's final
dimension from a barren, repetitive wasteland into a thriving and alien worldscape, filled with
life, light, adventure and mystery. Sadly, after almost five years of continued development,
the mod has been abandoned, and its codebase is a tangled, unmaintainable mess, spread across no
fewer than [four](https://github.com/quiqueck/BCLib)
[separate](https://github.com/quiqueck/WunderLib) [repos](https://github.com/quiqueck/WorldWeaver).

The goal of this project is **not** to be a continuation, faithful port or ground-up rewrite. It
is instead to create a simple, maintainable mod that can bring the _core aspects_ of BetterEnd's
ethos—that of a brighter dimension, teeming with life—to future versions of the game.

## Roadmap

Subject, of course, to change

- [x] **Proof of Concept**: Re-implement a select few BetterEnd blocks—at least one each of stone,
  soil, plant and crop—but no biomes or worldgen.
- [x] **Alpha 2**: First "features"—at least two trees, plus either a ruin or a lake—that can be
  `/place`d into a world, along with their associated blocks (read: wood sets) and at least one mob
- [ ] **Alpha 3**: An armored elytra (see below), bringing back the music discs, modifying
  vanilla loot tables
- [ ] **First Beta**: Introduce the first biomes and a start modifying the worldgen so stuff
  naturally spawns
- [ ] **First Release**: Enough worldgen that the experience can be enjoyed without needing to
  play in creative or roll one's own custom datapack

### Out of Scope

The completed version of LighterEnd:

- Will likely feature ore, but not Thallasium or Ender Ore (and thus, there will be no Terminite
  nor Aeternium)—these material types are either redundant or overpowered.
- Will not include Crystalite armor. Which, again, is overpowered.
- While there _will_ be an armored elytra, it will be heavily nerfed (with the glide decay of
  Aeternium elytra and sub-Diamond levels of protection) in order to balance it with vanilla
  elytra.
- Will only feature one type of end soil, though this soil may take on different appearances in
  different biomes, and bonemealing the soil in different biomes will produce different plants
- Will not include Eternal Portals
- Will not implement hammers, forging, infusing or alloying
- Will make the End Veil enchantment available exclusively as _extremely rare_ loot (cannot be
  obtained from villagers or the enchantment table)

### New Features

On the flip side, I do plan on implementing **new** features that were not present in the original
BetterEnd:

- Breeding villagers in The End will produce End Villagers, who will have exclusive professions,
  job sites and trades
- New trim materials (and possibly trim patterns)

## Contributing

**Worldgen datapack developers wanted!!** If you have experience creating custom dimensions
or adding biomes to existing dimensions, please contact me!

If there is a BetterEnd feature you would like to take responsibility for porting, please
[open an issue](https://github.com/OpenBagTwo/LighterEnd/issues/new) to start that discussion!

### Building the Mod from Source

0. Download and install a Java 21 OpenJDK such as [Temurin](https://adoptium.net/temurin/releases/)
1. Clone this repo
1. Load this project into your favorite Java IDE and run the "runDatagen" gradle task (or, from
   the command line, run `sh ./gradlew runDatagen` from the project root)
1. Now run the "build" task, either from the IDE or via  `sh ./gradlew build`
1. The compiled jar will be found under `build/libs`

### Style Guide

* This project uses [pre-commit](https://pre-commit.com/) hooks to format Markdown
  and non-generated JSON files. To set up pre-commit, follow the instructions linked above to
  install `pre-commit` on your system, then, from the repo root, run `pre-commit install` to
  have the hooks run on every commit.
* It is strongly recommended that you turn on automatic format on save / commit in your Java IDE.
  Instructions for doing that inside IntelliJ can be found
  [here](https://www.jetbrains.com/help/idea/reformat-and-rearrange-code.html#reformat-on-save).
  Make sure to select:
    * Reformat code
    * Optimize imports
      on any save.
* The top priority of this mod is to make it easy to understand and maintain (note that "difficult
  to update" and "tedious to update" are not the same thing). This means that implementations
  should be as "flat" as possible—no interfaces, the bare minimum of abstraction, and any "helper"
  methods should be used at least twice before they're refactored out into their own "library"
  class.
    * And, just to be extra clear: ***this mod should never depend on any other mod, library, API or
      project*** outside the Fabric API. If someone else already solved a thing, adapt how they
      did it (with proper attribution, and assuming it's GPL-compatible open source)—don't just
      count on that library always existing forever.
    * The corollary to the above is that this project will ***never*** be refactored into a
      general-purpose modding library or API. Anyone seeking to adapt the solutions developed for
      this mod is welcome to adapt those bits of code (subject to the license below).

## License and Acknowledgements

All code in this repository is licensed under
[GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html).

All assets (textures, models) were created by the BetterX team.

Music discs were composed, performed and recorded by Firel.

Many thanks to the excellent tutorial mods developed by
by [Kaupenjoe](https://github.com/Tutorials-By-Kaupenjoe/Fabric-Tutorial-1.21.X)
and [TurtyWurty](https://github.com/DaRealTurtyWurty/1.21-Tutorial-Mod) and
to [Pintér Gábor](https://github.com/pinter-gabor-at) and his
[IronSigns mod](https://gitlab.com/pintergabor/ironsigns) for providing an extremely helpful
example of adding custom signs.

You **may** use, modify and redistribute this mod, and you **may** include this mod within your
modpack or run it on a server, so long as you abide by the terms of
this license, which critically states that you **must** make the source code (including your
modifications to the mod) available to anyone downloading the mod
(including modified versions and including within a modpack)
