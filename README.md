# LighterEnd

![server + client mod](https://img.shields.io/badge/Server\/Client-both-purple)
![mod loader: fabric/quilt](https://img.shields.io/badge/Mod_Loader-fabric%2Fquilt-a4cc37)
[![build status](https://github.com/OpenBagTwo/LighterEnd/actions/workflows/build.yml/badge.svg)](https://github.com/OpenBagTwo/LighterEnd/actions/workflows/build.yml)
![supported versions](https://img.shields.io/badge/Supported_Versions-1.21.4-blue)
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
[separate](https://github.com/quiqueck/WunderLib) [repos](https://github.com/quiqueck/WorldWeaver.

The goal of this project is **not** to be a continuation, faithful port or ground-up rewrite. It
is instead to create a simple, maintainable mod that can bring the _core aspects_ of BetterEnd's
ethos—that of a brighter dimension, teeming with life—to future versions of the game.

Each component of the BetterEnd that makes its way into this mod will be selected carefully, with
intention, and reimplemented from scratch to take full advantage of the newest Minecraft features
while also not introducing unbalanced elements that would be overpowered compared to the vanilla
experience.

If you take issue with my decisions of what to include and exclude, the intention of my approach
to the project's design is to keep things simple, verbose and easily understandable so that it's
easy for even new Java programmers to fork this project and realize their own vision.

That said: I'm also open to contribution, collaboration and co-ownership, so if you'd like to
become actively involved in this project, please
[open an issue](https://github.com/OpenBagTwo/LighterEnd/issues/new) to kick off the discussioN!

## Roadmap

The plan for development roughly breaks down into three stages:

### The First Alpha Release

will be focused on reimplementing some core BetterEnd blocks—at least one each of stone, soil,
plant and crop—but will not introduce any new biomes or make any changes to the worldgen.
Included in this release will be some process—possibly manual—for editing the worldgen of the
Nullscape datapack to use these blocks as replacements for the Nether / Overworld blocks the
datapack uses by default.

### Beta Development

will define biomes and reintroduce mobs to those biomes. Rather than modify worldgen to include
these biomes, the beta builds will allow the player to **create** these biomes via some mechanism
akin to Minecraft Pocket's [Nether Reactor](https://minecraft.wiki/w/Nether_Reactor).

The idea is to create a progression where each biome will unlock access to blocks and mob loot
that can be used to create more biomes.

### Stable Release

Finally, the full "stable" release will include a pre-transformed End, pre-populated with the
biomes developed during Beta (the current plan is for the biome transformation mechanic to be
retained as an optional game mode / world type).

### Out of Scope

Again: the goal is **not** to recreate the full BetterEnd. The completed version of LighterEnd:

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
- Crafting a Reactor (full name TBD) will allow the player to change the biome of the surrounding
  area based on the presence of other blocks in the vicinity (_e.g._ amount of water nearby)
- New trim materials (and possibly trim patterns)


## Contributing

If there is a BetterEnd feature you would like to take responsibility for porting, please
[open an issue](https://github.com/OpenBagTwo/LighterEnd/issues/new) to start that discussion!

### Building the Mod from Source

0. Download and install a Java 21 OpenJDK such as [Temurin](https://adoptium.net/temurin/releases/)
1. Clone this repo
1. Load this project into your favorite Java IDE and run the "runDatagen" gradle task (or, from
   the command line, run `sh ./gradlew runDatagen` from the project root)
1. Now run the "build" task, either from the IDE or via  `sh ./gradlew build`
1. The compiled jar will be found under `build/libs`

## License and Acknowledgements

All code in this repository is licensed under
[GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html).

All assets (textures, models) were created by the BetterX team.

Music was composed, performed and recorded by Firel.

You **may** use, modify and redistribute this mod, and  you **may** include this mod within your
modpack or run it on a server, so long as you abide by the terms of
this license, which critically states that you **must** make the source code (including your
modifications to the mod) available to anyone downloading the mod
(including modified versions and including within a modpack)
