# LighterEnd

![server + client mod](https://img.shields.io/badge/Server\/Client-both-purple)
![mod loader: fabric/quilt](https://img.shields.io/badge/Mod_Loader-fabric%2Fquilt-a4cc37)
[![build status](https://github.com/OpenBagTwo/LighterEnd/actions/workflows/build.yml/badge.svg)](https://github.com/OpenBagTwo/LighterEnd/actions/workflows/build.yml)
![supported versions](https://img.shields.io/badge/Supported_Versions-1.21.4-blue)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/lighterend)](https://modrinth.com/mod/lighterend)

***A continuation mod for Paulevs' BetterEnd***

![logo](_static/logo_big.png)

## What Is This, and Why Does it Exist?

This mod is a ground-up reimplementation of a simplified selection of features from
[the BetterEnd mod](https://github.com/quiqueck/BetterEnd), which is no longer being maintained
or updated for modern versions of Minecraft. The goal of this project is **not** to reach feature
parity with the original mod, but simply to provide a path for its ethos—of a brighter, life-filled
End dimension—to carry on to modern versions of the game in a form that is cleanly structured and
easy to maintain.

## Roadmap

### Initial Release

A choice selection of blocks:
- Aurora Crystals
- Ender Blocks
- Violecite and Missing Tile Blocks
- End Moss
- Lumecorn

and a script to modify Nullscape to insert thses blocks into the worldgen

### Subsequent Releases

To be planned!


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
