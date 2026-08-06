🏹 HuntPlugin

A lightweight, dynamic bounty hunt plugin for Minecraft servers. Designed for PaperMC 1.21.1 using Java 21, this plugin allows server administrators to start a timed, server-wide manhunt on a specific player, complete with real-time tracking, anti-logout mechanics, and XP rewards.

Created by DevFly / BexindStudio.
✨ Features

    Dynamic Tab-List HUD: Updates every second to show the hunted player's name, exact coordinates, current dimension, a countdown timer, and time remaining until the next location ping.
    Anti-Combat Log: If the hunted player tries to disconnect to escape, they are instantly killed, their inventory drops on the ground, and the hunt ends.
    Visual Tracking: The hunted player receives the GLOWING potion effect for the entire duration of the hunt, making them visible to hunters through walls.
    Global Chat Broadcasting: Automatically broadcasts the target's coordinates and dimension in chat at configurable intervals.
    Reward System: If a player successfully kills the hunted target, they are rewarded with a configurable amount of XP levels (default: 75 levels). The plugin distinguishes between player kills and environmental deaths.
    Fully Configurable: All core mechanics can be adjusted in the config.yml without recompiling.

💻 Commands & Permissions
Command	Description	Permission
/hunt start <player>	Starts a timed hunt on the specified player.	hunt.admin
/hunt stop	Forcefully stops the current active hunt.	hunt.admin
/hunt help	Displays the help menu and plugin info.	hunt.admin

(By default, hunt.admin is only given to server operators).
⚙️ Configuration

The plugin generates a config.yml file inside the plugins/HuntPlugin folder upon first launch. 

Duration of the hunt in minuteshunt-duration: 25
How often to broadcast coordinates in chat (in seconds)broadcast-interval: 30
How many XP levels to give the killer upon successful huntxp-reward: 75

🛠️ Technical Details

     API: PaperMC API (Minecraft 1.21.1)
     Language: Java 21
     Build Tool: Maven

Skills Demonstrated

     Task Scheduling: Utilized Bukkit's runTaskTimer to handle real-time per-second countdowns and UI updates without impacting server TPS.
     Event API: Implemented custom Listener classes to intercept PlayerDeathEvent and PlayerQuitEvent for anti-logout and reward distribution mechanics.
     Configuration API: Used Bukkit's YAML configuration API to make the plugin dynamically read settings on enable.

👤 Author

DevFly (BexindStudio)

If you like this plugin and want to see more of my work, feel free to reach out!
text
 
1. In Zed, in the root folder of your project (where `pom.xml` is), create a new file named `README.md`.
2. Paste all the text above into it and save.
3. Now your project looks 100% professional! If you ever upload your code to GitHub, this file will automatically show up as the main description of your repository.
