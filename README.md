# LienubsTeam

![Documentation](https://github.com/Lienub-s-Plugins/LienubsTeam/actions/workflows/pages/pages-build-deployment/badge.svg)

My own vision of a team system.

## Table of contents

- Description
- Features
- How to contribute
- Useful links
- Contact

## Description

This project was created so that I could learn how to develop minecraft plugins. It doesn't pretend to be better or more efficient than another plugin of the same genre. It's up to you to use it or not!

## Features

### Commands

- `/team info`: display an Inventory GUI with the team information, such as the leader, member list. It is possible to click on an Online member to request a teleportation.
- `/team create <name>`: create a new team with name `name` if it doesn't already exist.
- `/team claim`: claim a chunk for your team, preventing non-member of the team to interact inside of it.
- `/team unclaim`: remove the behavior listed above.
- `/team invite <player>` invite a player to your team.
- `/team invite accept <team>` accept the invitation from team `team`
- `/team invite deny <team>` deny the invitation from team `team`
- `/team join <team>`send a join request to team `team`.
- `/team join accept <player>` accept join request from player `player`.
- `/team join deny <player>` deny join request from player `player`.
- `/team leave` leave the team you are currently in.
- `/team kick <player>`: kick the player `player` from the team.
- `/team tpa <player>`: send a teleport request to a member of your team.
- `/team tpa accept`: accept the last teleportation request sent.
- `/team tpa deny`: deny the last teleportation request sent.
- `/team kick <player>`: kick a player from the team.
- `/team promote <player>`: promote a player in your team (MEMBER -> MODERATOR)
- `/team demote <player>`: demote a player in your team (MODERATOR -> MEMBER)
- `/team transfer <player>`: transfer leadership of your team to another member (LEADER -> MODERATOR)

### Roles

Members of a team are separated into 3 roles:
- **LEADER**: Owner of the team. *Only one leader per team.*
> Permissions : `info, create, invite.accept, invite.deny, join, leave, accept, deny, tpa, join.request, invite.add, kick, claim, unclaim, join.accept, join.deny, promote, demote, disband, transfer`.
- **MODERATOR**: Managers of the team.
> Permissions : `info, create, invite.accept, invite.deny, join, leave, accept, deny, tpa, join.request, invite.add, kick, claim, unclaim, join.accept, join.deny`.
- **MEMBER**: simple member of the team.
> Permissions: `info, create, invite.accept, invite.deny, join, leave, accept, deny, tpa, join.request`.

Player not part of a team have the following permissions:
> `info, create, invite.accept, invite.deny, join, leave, accept, deny, join.request`

### How to contribute
You can post issue if you want, or you can fork if your goal is to have it your own way.

### Useful links

- [Plugin page](https://www.spigotmc.org/resources/lienubs-team.117194/)

### Contact

[SpigotMC](https://www.spigotmc.org/members/lienub.360534/)
