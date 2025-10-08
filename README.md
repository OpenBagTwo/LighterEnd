# LighterEnd

![server + client mod](https://img.shields.io/badge/Server\/Client-both-purple)
![mod loader: fabric/quilt](https://img.shields.io/badge/Mod_Loader-fabric%2Fquilt-a4cc37)
[![build status](https://github.com/OpenBagTwo/LighterEnd/actions/workflows/build.yml/badge.svg)](https://github.com/OpenBagTwo/LighterEnd/actions/workflows/build.yml)
![supported versions](https://img.shields.io/badge/Supported_Versions-1.21.5,1.21.10-blue)
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

The intent of this mod is **not** to be a 1-to-1 recreation of BetterEnd, and while no biomes
are explicitly being excluded, porting them is subject to interest (the developers' and the
community's). You can find a list of features slated for development, tied to the
["milestone"](https://github.com/OpenBagTwo/LighterEnd/milestones) (release) they're targeted for,
on the [issues page](https://github.com/OpenBagTwo/LighterEnd/issues).

If there's a specific biome of BetterEnd feature you'd like to see prioritized, feel free to open
an issue requesting it, after first having read through the following sections:

### Out of Scope

The completed version of LighterEnd:

- Will likely feature ore, but not Thallasium or Ender Ore (and thus, there will be no Terminite
  nor Aeternium)—these material types are either redundant or overpowered.
- Will not include Crystalite armor. Which, again, is overpowered.
- While the mod features an armored elytra, it is heavily nerfed (with the glide decay of
  Aeternium elytra and sub-Diamond levels of protection) in order to balance it with vanilla
  elytra
- Will only feature one type of end soil, though this soil may take on different appearances in
  different biomes, and bonemealing the soil in different biomes will produce different plants
- Will not include Eternal Portals
- Will not implement hammers, forging, infusing or alloying
- Will feature the End Veil effect, but solely as a potion effect, not as an enchantment

### New Features

On the flip side, LighterEnd has or will have features not present in BetterEnd

- [x] The option for gravity in The End to be 1/3 of normal
- [x] Silk Elytra—a craftable, trimmable and renewable armored elytra
- [x] New survival-challenge-friendly crafting recipes (such as the ability to get paper from end
  lily leaves and arrows from cubozoa drops)
- [x] Sniffers that sploot on End Moss will dig up rare End saplings
- [x] Obelisks that you can teleport to upon almost dying (meaning your stuff is safe even if you
  fall into The Void)
- [x] Ice stars will be more common and will contain ice enriched with metals (copper, gold, iron)
- [ ] New trim materials (and possibly trim patterns)

## Broad Compatibility

LighterEnd is compatible out-of-the-box with
both [Nullscape](https://modrinth.com/datapack/nullscape)
and [Moog's End Structures](https://modrinth.com/mod/mes-moogs-end-structures)

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

Substantial portions of this mod—including most of its assets—were adapted from BetterEnd and BCLib,
developed primarily by
[**Paulevs**](https://github.com/paulevsGitch) and [**quiqueck**](https://github.com/quiqueck),
in accordance with the terms of their respective licenses:

- https://github.com/quiqueck/BCLib/blob/9607e2e50818c9059505c32c72eb2d8d00bf6e9d/LICENSE
- https://github.com/quiqueck/BetterEnd/blob/00e4892827c4f1b0d2e213348f63e57a647a8011/LICENSE

The Chorus Crab was designed by **Pegnok** of the BetterX Discord.

Music discs were composed, performed and recorded by [Firel](https://www.youtube.com/@FirelMusic).

Many thanks:

- to the excellent tutorial mods developed by
  [Kaupenjoe](https://github.com/Tutorials-By-Kaupenjoe/Fabric-Tutorial-1.21.X) and
  [TurtyWurty](https://github.com/DaRealTurtyWurty/1.21-Tutorial-Mod)
- to [Pintér Gábor](https://github.com/pinter-gabor-at) and his
  [IronSigns mod](https://gitlab.com/pintergabor/ironsigns) for providing an extremely helpful
  example of adding custom signs
- to the [Enderscape](https://github.com/they-made-enderscape/enderscape) team for great modern
  examples of library-free worldgen and terrain modification
- to [Vanilla Tweaks](https://vanillatweaks.net/picker/resource-packs/) for the concept of grooved
  levers
- to the [BetterX Discord](https://discord.gg/kYuATbYbKW) for their feedback in shaping this mod's
  development

You **may** use, modify and redistribute this mod, and you **may** include this mod within your
modpack or run it on a server, so long as you abide by the terms of
this license, which critically states that you **must** make the source code (including your
modifications to the mod) available to anyone downloading the mod
(including modified versions and including within a modpack)
