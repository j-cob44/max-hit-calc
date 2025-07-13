/* NPCTypeWeakness.java
 * Enum for all NPC's with a type weakness.
 *
 *
 * Copyright (c) 2024, Jacob Burton <https://github.com/j-cob44>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.maxhitcalc;

import lombok.Getter;

import javax.swing.*;


/**
 *
 * Contains all known type bonuses against different NPC's.
 * Updated based on https://oldschool.runescape.wiki/w/Elemental_weakness
 * Last updated on 7/12/25
 *
 */

public enum NPCTypeWeakness
{
    None("None", SpellType.NoType, 0, null),
    // In Alphabetical Order
    AberrantSpectre("Aberrant Spectre", SpellType.Air, 50, null),
    AbhorrentSpectre("Abhorrent Spectre", SpellType.Air, 50, null),
    AbyssalPortal("Abyssal portal", SpellType.Fire, 50, NPCIcons.ABYSSAL_PORTAL_ICON),
    AdamantDragon("Adamant dragon", SpellType.Earth, 50, NPCIcons.ADAMANT_DRAGON_ICON),
    AgrithNaar("Agrith Naar", SpellType.Water, 25, null),
    Ahrim("Ahrim", SpellType.Air, 50, NPCIcons.AHRIM_ICON),
    AirElemental("Air Elemental", SpellType.Air, 30, null),
    AkkhasShadow("Akkha's Shadow", SpellType.Air, 60, null),
    AlbinoBat("Albino Bat", SpellType.Air, 40, null),
    AlchemicalHydra("Alchemical Hydra", SpellType.Earth, 50, null),
    AmmoniteCrab("Ammonite Crab", SpellType.Earth, 25, null),
    Amoxliatl("Amoxliatl", SpellType.Fire, 30, NPCIcons.AMOXLIATL),
    AncientWyvern("Ancient Wyvern", SpellType.Air, 35, null),
    AncientZygomite("Ancient Zygomite", SpellType.Fire, 40, null),
    AngryBarbarianSpirit("Angry Barbarian Spirit", SpellType.Air, 30, null),
    AnimatedArmour("Armour", SpellType.Earth, 30, null),
    AnimatedSpade("Animated Spade", SpellType.Earth, 25, null),
    Ankou("Ankou", SpellType.Air, 40, null),
    Apparition("Apparition", SpellType.Air, 40, null),
    Araxxor("Araxxor", SpellType.Fire, 50, NPCIcons.ARAXXOR_ICON),
    Araxyte("Araxyte", SpellType.Fire, 50, NPCIcons.ARAXXOR_ICON), // Needs Icon
    ArcaneScarab("Arcane scarab", SpellType.Fire, 50, NPCIcons.ARCANE_SCARAB_ICON),
    ArmadyleanGuard("Armadylean guard", SpellType.Air, 30, NPCIcons.AVIANSIE_ICON), // Needs Icon
    ArmouredZombie("Armoured Zombie", SpellType.Fire, 50, null),
    Arrg("Arrg", SpellType.Earth, 50, null),
    Asyn("Asyn", SpellType.Air, 40, null, true), // Shade and shadow
    Aviansie("Aviansie", SpellType.Air, 45, NPCIcons.AVIANSIE_ICON),
    BabyRoc("Baby Roc", SpellType.Air, 30, null),
    BabyTanglefoot("Baby Tanglefoot", SpellType.Fire, 20, null),
    BalfrugKreeyath("Balfrug Kreeyath", SpellType.Water, 25, null),
    Banshee("Banshee", SpellType.Air, 30, null),
    BarkBlamishSnail("Bark Blamish Snail", SpellType.Earth, 30, null),
    Basilisk("Basilisk", SpellType.Earth, 40, null),
    Bat("Bat", SpellType.Air, 35, null),
    BearCub("Bear Cub", SpellType.Fire, 25, null),
    Berry("Berry", SpellType.Earth, 20, null),
    BerserkBarbarianSpirit("Berserk Barbarian Spirit", SpellType.Air, 25, null),
    BigWolf("Big Wolf", SpellType.Fire, 20, null),
    BigFrog("Big Frog", SpellType.Earth, 25, null),
    BillyGoat("Billy Goat", SpellType.Fire, 10, null),
    Bird("Bird", SpellType.Air, 25, null),
    BlackBear("Black Bear", SpellType.Fire, 30, null),
    BlackDemon("Black demon", SpellType.Water, 40, NPCIcons.BLACK_DEMON_ICON),
    BlackDragon("Black Dragon", SpellType.Water, 50, NPCIcons.BLACK_DRAGON_ICON, true), // Brutal, Baby, and normal black dragons all have the same %
    BlackJaguar("Black Jaguar", SpellType.Fire, 30, null),
    BlessedSpider("Blessed Spider", SpellType.Fire, 50, null),
    BloodBlamishSnail("Blood Blamish Snail", SpellType.Earth, 30, null),
    BloodMoon("Blood Moon", SpellType.Air, 15, null),
    BloodJaguar("Blood Jaguar", SpellType.Earth, 15, null),
    BloodSpawn("Blood Spawn", SpellType.Earth, 50, null),
    BlueMoon("Blue Moon", SpellType.Air, 15, null),
    BlueDragon("Blue Dragon", SpellType.Water, 50, NPCIcons.BLUE_DRAGON_ICON, true), // Brutal, Baby, and normal blue dragons all have the same %
    Boar("Boar", SpellType.Fire, 20, null),
    BodyGolem("Body Golem", SpellType.Earth, 35, null),
    BronzeDragon("Bronze dragon", SpellType.Earth, 50, NPCIcons.BRONZE_DRAGON_ICON),
    BrandaTheFireQueen("Branda the Fire Queen", SpellType.Water, 50, null),
    BruiseBlamishSnail("Bruise Blamish Snail", SpellType.Earth, 25, null),
    Bryophyta("Bryophyta", SpellType.Fire, 50, null),
    Callisto("Cerberus", SpellType.Fire, 30, null),
    CaveBug("Cave Bug", SpellType.Fire, 50, null),
    CaveGoblin("Cave Goblin", SpellType.Fire, 40, null, true), // normal, guard, and miner
    CaveHorror("Cave Horror", SpellType.Fire, 30, null),
    CaveKraken("Cave Kraken", SpellType.Earth, 50, null),
    CaveSlime("Cave Slime", SpellType.Earth, 50, null),
    Cerberus("Cerberus", SpellType.Water, 40, NPCIcons.CERBERUS_ICON),
    ChampionOfScabaras("Champion of Scabaras", SpellType.Fire, 35, null),
    ChaosElemental("Chaos Elemental", SpellType.Air, 50, null),
    ChaosGolem("Chaos Golem", SpellType.Earth, 35, null),
    ChilledJelly("Chilled jelly", SpellType.Fire, 50, NPCIcons.CHILLED_JELLY_ICON),
    ChokeDevil("Choke Devil", SpellType.Air, 25, null),
    Chronozon("Chronozon", SpellType.Water, 25, null),
    ColossalHydra("Colossal Hydra", SpellType.Earth, 40, null),
    CorporealBeast("Corporeal Beast", SpellType.Earth, 10, null),
    Crab("Crab", SpellType.Earth, 20, null),
    Crocodile("Crocodile", SpellType.Earth, 40, null),
    CryptSpider("Crypt Spider", SpellType.Fire, 50, null),
    Dad("Dad", SpellType.Earth, 40, null),
    Dagannoth("Dagannoth", SpellType.Earth, 35, null, true), // normal, prime, rex, supreme, fledgling, mother, and spawn
    DarkAnkou("Dark Ankou", SpellType.Air, 20, null),
    DarkBeast("Dark Beast", SpellType.Earth, 60, null),
    Dawn("Dawn", SpellType.Earth, 70, null),
    DeadlyRedSpider("Deadly Red Spider", SpellType.Fire, 40, null),
    DeathWing("Death wing", SpellType.Air, 20, null),
    Delrith("Delrith", SpellType.Water, 15, null),
    DemonicGorilla("Demonic gorilla", SpellType.Water, 35, NPCIcons.DEMONIC_GORILLA_ICON),
    DeviantSpectre("Deviant Spectre", SpellType.Air, 30, null),
    Dharok("Dharok", SpellType.Air, 50, NPCIcons.DHAROK_ICON),
    DontKnowWhat("Don't Know What", SpellType.Fire, 40, NPCIcons.DHAROK_ICON),
    Doomion("Doomion", SpellType.Water, 25, NPCIcons.DHAROK_ICON),
    Drake("Drake", SpellType.Water, 50, NPCIcons.DRAKE_ICON),
    Dusk("Dusk", SpellType.Earth, 40, null),
    DustDevil("Dust Devil", SpellType.Air, 35, null),
    EarthWarriorChampion("Earth Warrior Champion", SpellType.Earth, 50, null),
    EarthElemental("Earth Elemental", SpellType.Earth, 50, null),
    EarthWarrior("Earth Warrior", SpellType.Earth, 50, null),
    EclipseMoon("Eclipse Moon", SpellType.Air, 15, null),
    EldricTheIceKing("Eldric the Ice King", SpellType.Fire, 50, null),
    ElidinisWarden("Elidinis' Warden", SpellType.Earth, 50, null),
    Elvarg("Elvarg", SpellType.Water, 30, null),
    EnragedBarbarianSpirit("Enraged Barbarian Spirit", SpellType.Air, 15, null),
    Ent("Ent", SpellType.Fire, 40, null),
    EntranaFirebird("Entrana Firebird", SpellType.Water, 100, null),
    EvilSpirit("Evil Spirit", SpellType.Air, 30, null),
    Fareed("Fareed", SpellType.Water, 30, null),
    FerociousBarbarianSpirit("Ferocious Barbarian Spirit", SpellType.Water, 30, null),
    FeverSpider("Fever Spider", SpellType.Fire, 25, null),
    FireWarriorOfLesarkus("Fire Warrior of Lesarkus", SpellType.Water, 100, null),
    FireElemental("Fire Elemental", SpellType.Water, 100, null),
    FireGiant("Fire giant", SpellType.Water, 100, NPCIcons.FIRE_GIANT_ICON),
    FiyrShade("Fiyr Shade", SpellType.Air, 40, null),
    Flambeed("Flambeed", SpellType.Water, 50, null),
    FlawedGolem("Flawed Golem", SpellType.Earth, 35, null),
    FleshCrawler("Flesh Crawler", SpellType.Fire, 20, null),
    FlightKilisa("Flight kilisa", SpellType.Air, 30, NPCIcons.AVIANSIE_ICON), // Needs icon
    FlockleaderGeerin("Flockleader geerin", SpellType.Air, 30, NPCIcons.AVIANSIE_ICON), // Needs icon
    Flower("Flower", SpellType.Fire, 100, null),
    ForgottenSoul("Forgotten Soul", SpellType.Air, 50, null),
    Fox("Fox", SpellType.Fire, 25, null),
    FrenziedIceTroll("Frenzied Ice Troll", SpellType.Fire, 50, null, true), // female, grunt, male, and runt
    Frog("Frog", SpellType.Earth, 40, null),
    Frogeel("Frogeel", SpellType.Earth, 40, null),
    FrostCrab("Frost Crab", SpellType.Fire, 100, NPCIcons.FROST_CRAB),
    FrostNagua("Frost Nagua",SpellType.Fire, 50, NPCIcons.FROST_NAGUA),
    Galvek("Galvek", SpellType.Water, 40, null),
    Gargoyle("Gargoyle", SpellType.Earth, 40, null, true), // normal and marble
    GeneralGraardor("General Graardor", SpellType.Earth, 40, null),
    Ghast("Ghast", SpellType.Air, 25, null),
    Ghost("Ghost", SpellType.Air, 50, NPCIcons.GHOST_ICON),
    GiantBat("Giant Bat", SpellType.Air, 10, null),
    GiantCryptSpider("Giant Crypt Spider", SpellType.Fire, 40, null),
    GiantFrog("Giant Frog", SpellType.Earth, 25, null),
    GiantLobster("Giant Lobster", SpellType.Earth, 25, null),
    GiantMole("Giant mole", SpellType.Water, 50, NPCIcons.GIANT_MOLE_ICON),
    GiantMosquito("Giant Mosquito", SpellType.Fire, 40, null),
    GiantRoc("Giant Roc", SpellType.Air, 35, null),
    GiantRockCrab("Giant Rock Crab", SpellType.Earth, 20, null),
    GiantRockslug("Giant Rockslug", SpellType.Earth, 20, null),
    GiantScarab("Giant Scarab", SpellType.Fire, 50, null),
    GiantSkeleton("Giant Skeleton", SpellType.Earth, 40, null),
    GiantSnail("Giant Snail", SpellType.Earth, 45, null),
    GiantSpider("Giant spider", SpellType.Fire, 50, NPCIcons.GIANT_SPIDER_ICON),
    Glod("Glod", SpellType.Earth, 50, null),
    GraveScorpion("Grave Scorpion", SpellType.Fire, 40, null),
    GreaterDemon("Greater Demon", SpellType.Water, 40, NPCIcons.GREATER_DEMON_ICON),
    GreaterSkeletonHellhound("Greater Skeleton Hellhound", SpellType.Earth, 40, null),
    GreatOlm("Great Olm", SpellType.Earth, 50, null),
    GreenDragon("Green Dragon", SpellType.Water, 50, NPCIcons.GREEN_DRAGON_ICON, true), // Brutal, Baby, and normal blue dragons all have the same %
    GrizzlyBear("Grizzly Bear", SpellType.Fire, 20, null), // normal and cub
    Guthan("Guthan", SpellType.Air, 50, NPCIcons.GUTHAN_ICON),
    HarpieBugSwarm("Harpie Bug Swarm", SpellType.Fire, 50, null),
    Hellhound("Hellhound", SpellType.Water, 50, NPCIcons.HELLHOUND_ICON),
    Hespori("Hespori", SpellType.Fire, 100, NPCIcons.HESPORI_ICON),
    HillGiant("Hill Giant", SpellType.Earth, 25, null),
    Holthion("Holthion", SpellType.Water, 10, null),
    HopelessCreature("Hopeless Creature", SpellType.Fire, 40, null),
    Hueycoatl("Hueycoatl", SpellType.Earth, 60, NPCIcons.HUEYCOATL_ICON, true), // all entities of it
    HugeSpider("Huge Spider", SpellType.Fire, 40, null),
    Hydra("Hydra", SpellType.Earth, 40, null),
    IceDemon("Ice demon", SpellType.Fire, 150, NPCIcons.ICE_DEMON_ICON),
    Icefiend("Icefiend", SpellType.Fire, 100, NPCIcons.ICEFIEND_ICON),
    IceGiant("Ice giant", SpellType.Fire, 100, NPCIcons.ICE_GIANT_ICON),
    Icelord("Icelord", SpellType.Fire, 30, null),
    IceQueen("Ice Queen", SpellType.Fire, 100, null),
    IceSpider("Ice Spider", SpellType.Fire, 100, NPCIcons.ICE_SPIDER_ICON),
    IceTroll("Ice Troll", SpellType.Fire, 100, NPCIcons.ICE_TROLL_ICON, true), // Ordinary, Male, and Female all have the same %
    IceTrollGrunt("Ice Troll Grunt", SpellType.Fire, 35, NPCIcons.ICE_TROLL_ICON),
    IceTrollKing("Ice Troll King", SpellType.Fire, 35, NPCIcons.ICE_TROLL_ICON),
    IceTrollRunt("Ice Troll Runt", SpellType.Fire, 50, NPCIcons.ICE_TROLL_RUNT_ICON),
    IceWarrior("Ice warrior", SpellType.Fire, 100, NPCIcons.ICE_WARRIOR_ICON),
    IceWolf("Ice Wolf", SpellType.Fire, 20, null),
    Imp("Imp", SpellType.Water, 10, null),
    ImpChampion("Imp Champion", SpellType.Water, 10, null),
    InfernalPyrelord("Infernal Pyrelord", SpellType.Water, 10, null),
    IronDragon("Iron dragon", SpellType.Earth, 50, NPCIcons.IRON_DRAGON_ICON),
    IrvigSenay("Irvig Senay", SpellType.Air, 15, null),
    Jackal("Jackal", SpellType.Fire, 10, null),
    Jaguar("Jaguar", SpellType.Fire, 10, null), // don't use partial
    JaguarCub("Jaguar Cub", SpellType.Fire, 10, null),
    Jal("Jal-", SpellType.Water, 40, null, true), // multiple tzhaar jal-
    JalTokJad("JalTok-Jad", SpellType.Water, 40, null),
    Jelly("Jelly", SpellType.Earth, 35, null),
    Jogre("Jogre", SpellType.Fire, 25, null), // normal and champion
    Jormungand("Jormungand", SpellType.Earth, 40, null),
    JudgeOfYama("Judge of Yama", SpellType.Water, 15, null),
    JungleDemon("Jungle Demon", SpellType.Fire, 25, null),
    JungleWolf("Jungle Wolf", SpellType.Fire, 15, null),
    JungleHorror("Jungle Horror", SpellType.Fire, 25, null),
    JungleSpider("Jungle Spider", SpellType.Fire, 25, null),
    Kalphite("Kalphite", SpellType.Fire, 40, null, true), // normal, Guardian, QUeen, Soldier, and Worker
    Kalrag("Kalrag", SpellType.Fire, 10, null),
    Kamil("Kamil", SpellType.Fire, 10, null),
    Karamel("Karamel", SpellType.Fire, 40, null),
    Karil("Karil", SpellType.Air, 50, NPCIcons.KARIL_ICON),
    KasondeTheCraven("Kasonde the Craven", SpellType.Air, 15, null),
    KetZek("Ket-Zek", SpellType.Water, 40, null),
    KetlaTheUnworthy("Ketla the Unworthy", SpellType.Air, 15, null),
    Kephri("Kephri", SpellType.Fire, 35, NPCIcons.KEPHRI_ICON), // 40% with shield, not possible to calculate
    KhazardScorpion("Khazard Scorpion", SpellType.Fire, 25, null),
    Killerwatt("Killerwatt", SpellType.Air, 60, null),
    KingBlackDragon("King black dragon", SpellType.Water, 50, NPCIcons.KBD_ICON),
    KingSandCrab("King Sand Crab", SpellType.Earth, 15, null),
    KingScorpion("King Scorpion", SpellType.Fire, 20, null),
    Kob("Kob", SpellType.Earth, 20, null),
    Kraka("Kraka", SpellType.Earth, 20, null),
    Kraken("Kraken", SpellType.Earth, 50, null),
    Kreearra("Kree'arra", SpellType.Air, 30, NPCIcons.KREE_ICON),
    KrilTsutsaroth("K'ril Tsutsaroth", SpellType.Water, 30, NPCIcons.KRIL_ICON),
    LargeMosquito("Large Mosquito", SpellType.Fire, 40, null),
    LavaDragon("Lava dragon", SpellType.Water, 50, NPCIcons.LAVA_DRAGON_ICON),
    LesserDemon("Lava dragon", SpellType.Water, 40, NPCIcons.LESSER_DEMON_ICON),
    LoarShade("Loar Shade", SpellType.Air, 40, null),
    Lobstrosity("Lobstrosity", SpellType.Earth, 25, null),
    Locust("Locust", SpellType.Earth, 40, null),
    LocustRider("Locust Rider", SpellType.Earth, 25, null),
    LongTailedWyvern("Long-tailed Wyvern", SpellType.Air, 25, null),
    MindGolem("Mind Golem", SpellType.Earth, 35, null),
    MithrilDragon("Mithril dragon", SpellType.Earth, 50, NPCIcons.MITHRIL_DRAGON_ICON),
    Mogre("Mogre", SpellType.Earth, 20, null),
    Molanisk("Molanisk", SpellType.Earth, 20, null),
    MonkeyZombie("Monkey Zombie", SpellType.Fire, 50, null),
    MonstrousBasilisk("Monstrous Basilisk", SpellType.Earth, 40, null),
    MossGiant("Moss Giant", SpellType.Fire, 50, NPCIcons.MOSS_GIANT_ICON),
    MossGuardian("Moss Guardian", SpellType.Fire, 50, null),
    Mother("Mother", SpellType.Fire, 20, null),
    MountainTroll("Mountain Troll", SpellType.Earth, 50, NPCIcons.MOUNTAIN_TROLL_ICON),
    Mudskipper("Mudskipper", SpellType.Earth, 20, null),
    Mummy("Mummy", SpellType.Fire, 50, null),
    MutatedTortoise("Mutated Tortoise", SpellType.Earth, 25, null),
    Muttadile("Muttadile", SpellType.Earth, 40, null),
    MyreBlamishSnail("Myre Blamish Snail", SpellType.Earth, 25, null),
    Nazastarool("Nazastarool", SpellType.Air, 20, null),
    Nezikchened("Nezikchened", SpellType.Water, 10, null),
    NuclearSmokeDevil("Nuclear Smoke Devil", SpellType.Air, 20, null),
    Nylocas("Nylocas", SpellType.Fire, 15, null, true), // Athanatos, Hagios, Ischyros, Matomenos, Prikipas, Toxobolos, and Vasilias
    Obor("Obor", SpellType.Earth, 20, null),
    OchreBlamishSnail("Ochre Blamish Snail", SpellType.Earth, 25, null),
    Ogre("Ogre", SpellType.Earth, 20, null, true), // normal, chieftain, shaman, ogress and reanimated
    Ork("Ork", SpellType.Earth, 15, null),
    Othainian("Othainian", SpellType.Water, 10, null),
    OtherworldlyBeing("Otherworldly Being", SpellType.Air, 35, null),
    Parasite("Parasite", SpellType.Fire, 50, null),
    PeeHat("Pee Hat", SpellType.Earth, 20, null),
    Penguin("Penguin", SpellType.Fire, 25, null),
    PerstenTheDeceitful("Persten the Deceitful", SpellType.Air, 15, null),
    PhantomMuspah("Phantom Muspah", SpellType.Air, 65, null),
    PhrinShade("Phrin Shade", SpellType.Air, 40, null),
    PitScorpion("Pit Scorpion", SpellType.Fire, 25, null),
    PoisonScorpion("Poison Scorpion", SpellType.Fire, 25, null),
    PoisonSpider("Poison Spider", SpellType.Fire, 40, null),
    Porcupine("Porcupine", SpellType.Fire, 20, null),
    Pyrefiend("Pyrefiend", SpellType.Water, 100, NPCIcons.PYREFIEND_ICON),
    Pyrelord("Pyrelord", SpellType.Water, 100, null),
    Rabbit("Rabbit", SpellType.Fire, 15, null),
    Ram("Ram", SpellType.Fire, 20, null),
    RanalphDevere("Ranalph Devere", SpellType.Air, 10, null),
    ReanimatedAviansie("Reanimated Aviansie", SpellType.Air, 30, null),
    ReanimatedBear("Reanimated Bear", SpellType.Fire, 20, null),
    ReanimatedDagannoth("Reanimated Dagannoth", SpellType.Earth, 50, null),
    ReanimatedDemon("Reanimated Demon", SpellType.Water, 10, null),
    ReanimatedDragon("Reanimated Dragon", SpellType.Water, 50, null),
    ReanimatedGiant("Reanimated Giant", SpellType.Earth, 20, null),
    ReanimatedGoblin("Reanimated Goblin", SpellType.Fire, 10, null),
    ReanimatedHellhound("Reanimated Hellhound", SpellType.Water, 10, null),
    ReanimatedImp("Reanimated Imp", SpellType.Water, 10, null),
    ReanimatedKalphite("Reanimated Kalphite", SpellType.Fire, 50, null),
    ReanimatedScorpion("Reanimated Scorpion", SpellType.Fire, 25, null),
    RedDragon("Red Dragon", SpellType.Water, 50, NPCIcons.RED_DRAGON_ICON, true), // Brutal, Baby, and normal black dragons all have the same %
    RepugnantSpectre("Repugnant Spectre", SpellType.Air, 40, null),
    Revenant("Revenant", SpellType.Air, 30, null),
    Riyl("Riyl", SpellType.Air, 40, null, true), // Shade and shadow
    RockCrab("Rock Crab", SpellType.Earth, 20, null),
    RockGolem("Rock Golem", SpellType.Earth, 40, null),
    RockLobster("Rock Lobster", SpellType.Earth, 25, null),
    Rockslug("Rockslug", SpellType.Earth, 25, null),
    RuneDragon("Rune dragon", SpellType.Earth, 50, NPCIcons.RUNE_DRAGON_ICON),
    RuniteGolem("Runite Golem", SpellType.Earth, 60, null),
    SanTojalon("San Tojalon", SpellType.Air, 10, null),
    SandCrab("Sand Crab", SpellType.Earth, 20, null),
    SandSnake("Sand Snake", SpellType.Air, 40, null),
    Sarachnis("Sarachnis", SpellType.Fire, 40, null),
    Scarab("Scarab", SpellType.Fire, 40, null),
    ScarabMage("Scarab mage", SpellType.Fire, 50, NPCIcons.SCARAB_MAGE_ICON),
    ScarabSwarm("Scarab swarm", SpellType.Fire, 40, null),
    ScarredImp("Scarred Imp", SpellType.Water, 10, null),
    ScarredLesserDemon("Scarred Lesser Demon", SpellType.Water, 10, null),
    Scorpia("Scorpia", SpellType.Fire, 35, null),
    Scorpion("Scorpion", SpellType.Fire, 25, null),
    ScreamingBanshee("Screaming Banshee", SpellType.Air, 40, null),
    ScreamingTwistedBanshee("Screaming Twisted Banshee", SpellType.Air, 40, null),
    SeaSnake("Sea Snake", SpellType.Earth, 20, null, true), // Giant, young and Hatchling
    SeaTroll("Sea Troll", SpellType.Earth, 15, null, true), // normal and queen
    Seagull("Seagull", SpellType.Air, 20, null),
    Shade("Shade", SpellType.Air, 40, null),
    Shadow("Shadow", SpellType.Air, 40, null),
    ShadowWyrm("Shadow Wyrm", SpellType.Air, 50, null),
    ShadowSpider("Shadow Spider", SpellType.Air, 40, null),
    SkeletalWyvern("Skeletal Wyvern", SpellType.Fire, 25, null),
    Skeleton("Skeleton", SpellType.Earth, 40, NPCIcons.SKELETON_ICON, true), // normal, mage, brute, fremennik, guard, heavy, hero, thug, champion
    SkeletonHellhound("Skeleton Hellhound", SpellType.Earth, 30, null),
    SkeletonWarlord("Skeleton Warlord", SpellType.Earth, 20, null),
    Slagilith("Shadow", SpellType.Earth, 40, null),
    SmallScarab("Small Scarab", SpellType.Fire, 50, null),
    SmokeDevil("Smoke Devil", SpellType.Air, 30, null),
    SoldierScarab("Soldier scarab", SpellType.Fire, 50, NPCIcons.SOLDIER_SCARAB_ICON),
    Spidine("Spidine", SpellType.Fire, 25, null),
    Spindel("Spindel", SpellType.Fire, 25, null, true), // Spindel and spiderlings
    SpiritualMage("Spiritual Mage", SpellType.Air, 30, null),
    SpiritualRanger("Spiritual Ranger", SpellType.Air, 30, null),
    SpiritualWarrior("Spiritual Warrior", SpellType.Air, 30, null),
    SpittingWyvern("Spitting Wyvern", SpellType.Air, 25, null),
    SpittingScarab("Spitting scarab", SpellType.Fire, 50, NPCIcons.SPITTING_SCARAB_ICON),
    SteelDragon("Steel dragon", SpellType.Earth, 50, NPCIcons.STEEL_DRAGON_ICON),
    StoneGuardian("Stone Guardian", SpellType.Earth, 40, null),
    Strangled("Strangled", SpellType.Fire, 50, null),
    StrangledBear("Strangled Bear", SpellType.Fire, 60, null),
    StrangledBoar("Strangled Boar", SpellType.Fire, 60, null),
    StrangledLynx("Strangled Lynx", SpellType.Fire, 60, null),
    SulphurNagua("Sulphur Nagua", SpellType.Air, 15, null),
    SummonedZombie("Summoned Zombie", SpellType.Fire, 50, null),
    Suqah("Suqah", SpellType.Earth, 20, null),
    SwampCrab("Swamp Crab", SpellType.Earth, 35, null),
    SwampFrog("Swamp Frog", SpellType.Earth, 20, null),
    TalonedWyvern("Taloned Wyvern", SpellType.Air, 35, null),
    Tanglefoot("Tanglefoot", SpellType.Fire, 35, null),
    Tekton("Tekton", SpellType.Water, 20, null),
    TempleSpider("Temple Spider", SpellType.Fire, 40, null),
    TheDraugen("The Draugen", SpellType.Air, 35, null),
    TheForsakenAssassin("The Forsaken Assassin", SpellType.Air, 15, null),
    ThermonuclearSmokeDevil("Thermonuclear Smoke Devil", SpellType.Air, 40, null),
    TokXil("Tok-Xil", SpellType.Water, 40, null),
    Torag("Torag", SpellType.Air, 50, NPCIcons.TORAG_ICON),
    TormentedDemon("Tormented Demon", SpellType.Water, 30, null),
    TormentedSoul("Tormented Soul", SpellType.Air, 20, null),
    Tortoise("Tortoise", SpellType.Earth, 20, null),
    TrappedSoul("Trapped Soul", SpellType.Air, 15, null),
    TreeSpirit("Tree Spirit", SpellType.Fire, 35, null),
    TreusDayth("Treus Dayth", SpellType.Air, 25, null),
    Troll("Troll", SpellType.Earth, 20, null, true), // normal, reanimated, river, thrower, general, spectator
    TstanonKarlak("Tstanon Karlak", SpellType.Water, 10, null),
    TumekensWarden("Tumeken's Warden", SpellType.Earth, 50, null),
    TwistedBanshee("Twisted Banshee", SpellType.Air, 25, null),
    TzKek("Tz-Kek", SpellType.Water, 40, null),
    TzKih("Tz-Kih", SpellType.Water, 40, null),
    TzHaar("TzHaar-", SpellType.Water, 40, null, true), // reanimated, Hur, Ket, Mej, Xil
    TzKal("TzKal-", SpellType.Water, 40, null),
    TzTokJad("TzTok-Jad", SpellType.Water, 40, null),
    Undead("Undead", SpellType.Fire, 50, null), // druid, lumberjack, zealot, chicken, cow, one
    UriumShade("Urium Shade", SpellType.Air, 40, null),
    Vardorvis("Vardorvis", SpellType.Fire, 35, null),
    Venenatis("Venenatis", SpellType.Fire, 40, null, true), // venenatis and spiderlings
    Verac("Verac", SpellType.Air, 50, NPCIcons.VERAC_ICON),
    // verzik vitur phase 3?
    VespineSoldier("Vespine Soldier", SpellType.Fire, 40, null),
    Vespula("Vespula", SpellType.Fire, 50, NPCIcons.VESPULA_ICON),
    VitreousJelly("Vitreous Jelly", SpellType.Earth, 30, null),
    Vorkath("Vorkath", SpellType.Water, 40, null),
    Vulture("Vulture", SpellType.Air, 20, null),
    Wallasalki("Wallasalki", SpellType.Earth, 30, null),
    WarpedJelly("Warped Jelly", SpellType.Earth, 30, null, true), // normal and Vitreous
    WarpedTortoise("Warped Tortoise", SpellType.Earth, 20, null),
    WaterElemental("Water Elemental", SpellType.Earth, 50, null),
    Waterfiend("Waterfiend", SpellType.Earth, 50, NPCIcons.WATERFIEND_ICON),
    TheWhisperer("The Whisperer", SpellType.Earth, 60, null),
    WhiteWolf("WhiteWolf", SpellType.Fire, 25, null),
    Wolf("Wolf", SpellType.Fire, 25, null), // dont use partial
    Wyrm("Wyrm", SpellType.Air, 50, NPCIcons.WYRM_ICON),
    Wyrmling("Wyrmling", SpellType.Air, 50, NPCIcons.WYRMLING_ICON),
    Xarpus("Xarpus", SpellType.Air, 50, null),
    YtHurKot("Yt-HurKot", SpellType.Water, 40, null),
    YtMejKot("Yt-MejKot", SpellType.Water, 40, null),
    ZaklnGritch("Zakl'n Gritch", SpellType.Water, 30, null),
    Zebak("Zebak", SpellType.Earth, 40, null),
    Zombie("Zombie", SpellType.Fire, 50, null, true), // normal, pirate, rat, swab, champion
    ZombifiedSpawn("ZombifiedSpawn", SpellType.Fire, 50, null),
    Zulrah("Zulrah", SpellType.Fire, 50, NPCIcons.ZULRAH_ICON),
    Zygomite("Zygomite", SpellType.Fire, 40, null);

    @Getter
    private final String NPCName;
    @Getter
    private final SpellType elementalWeakness;
    @Getter
    private final int weaknessPercent;
    @Getter
    private final ImageIcon icon;
    @Getter
    private final boolean allowPartialSearch;

    /**
     * Gets an NPC's Weakness by searching their name
     * @param name string
     * @return NPCTypeWeakness, or null for failure
     */
    public static NPCTypeWeakness findWeaknessByName(String name)
    {
        if(name == null)
            return null;

        // Search exact name first
        for (NPCTypeWeakness npc : NPCTypeWeakness.values())
        {
            if(npc.NPCName.equalsIgnoreCase(name)){
                return npc;
            }
        }

        // Search names that aren't complete, e.g: baby (black dragon)
        for (NPCTypeWeakness npc : NPCTypeWeakness.values())
        {
            if(npc.allowPartialSearch)
            {
                if(name.toLowerCase().contains(npc.NPCName.toLowerCase())){
                    return npc;
                }
            }
        }

        // error, failure
        return null;
    }

    NPCTypeWeakness(String NPCName, SpellType elementalWeakness, int weaknessPercent, ImageIcon icon)
    {
        this.NPCName = NPCName;
        this.elementalWeakness = elementalWeakness;
        this.weaknessPercent = weaknessPercent;
        this.icon = icon;
        this.allowPartialSearch = false;
    }

    NPCTypeWeakness(String NPCName, SpellType elementalWeakness, int weaknessPercent, ImageIcon icon, boolean allowPartialSearch)
    {
        this.NPCName = NPCName;
        this.elementalWeakness = elementalWeakness;
        this.weaknessPercent = weaknessPercent;
        this.icon = icon;
        this.allowPartialSearch = allowPartialSearch;
    }
}
