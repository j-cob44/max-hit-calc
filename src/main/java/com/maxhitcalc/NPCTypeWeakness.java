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
 * Contains all known type bonuses and flat armours against different NPCs.
 * Updated based on https://oldschool.runescape.wiki/w/Elemental_weakness and https://oldschool.runescape.wiki/w/Flat_armour
 * Last updated on 10/22/26
 *
 */

public enum NPCTypeWeakness
{
    None("None", SpellType.NoType, 0),
    // In Alphabetical Order
    AberrantSpectre("Aberrant Spectre", SpellType.Air, 50),
    AbhorrentSpectre("Abhorrent Spectre", SpellType.Air, 50),
    AbyssalPortal("Abyssal Portal", SpellType.Fire, 50),
    AcidicAraxyte("Acidic Araxyte", SpellType.Fire, 50),
    AdamantDragon("Adamant Dragon", SpellType.Earth, 50),
    AgrithNaar("Agrith Naar", SpellType.Water, 25),
    Ahrim("Ahrim", SpellType.Air, 50, true),
    AirElemental("Air Elemental", SpellType.Air, 30),
    AkkhasShadow("Akkha's Shadow", SpellType.Air, 60),
    AlbinoBat("Albino Bat", SpellType.Air, 40),
    AlchemicalHydra("Alchemical Hydra", SpellType.Earth, 50),
    AmmoniteCrab("Ammonite Crab", SpellType.Earth, 25),
    Amoxliatl("Amoxliatl", SpellType.Fire, 30, -2),
    AncientCustodian("Ancient Custodian", SpellType.Fire, 30),
    AncientWyvern("Ancient Wyvern", SpellType.Air, 35),
    AncientZygomite("Ancient Zygomite", SpellType.Fire, 40),
    AngryBarbarianSpirit("Angry Barbarian Spirit", SpellType.Air, 30),
    AnimatedArmour("Armour", SpellType.Earth, 30, true), // all metal types
    AnimatedSpade("Animated Spade", SpellType.Earth, 25),
    Ankou("Ankou", SpellType.Air, 40),
    Apparition("Apparition", SpellType.Air, 40),
    Araxxor("Araxxor", SpellType.Fire, 50),
    Araxyte("Araxyte", SpellType.Fire, 50),
    ArcaneScarab("Arcane Scarab", SpellType.Fire, 50),
    ArmadyleanGuard("Armadylean Guard", SpellType.Air, 30),
    ArmouredZombie("Armoured Zombie", SpellType.Fire, 50, 2),
    Arrg("Arrg", SpellType.Earth, 50),
    Asyn("Asyn", SpellType.Air, 40, true), // Shade and shadow
    AsynShade("Asyn Shade", SpellType.Air, 40, -3),
    Aviansie("Aviansie", SpellType.Air, 45),
    BabyRoc("Baby Roc", SpellType.Air, 30),
    BabyTanglefoot("Baby Tanglefoot", SpellType.Fire, 20),
    BalfrugKreeyath("Balfrug Kreeyath", SpellType.Water, 25),
    Banshee("Banshee", SpellType.Air, 30),
    BarkBlamishSnail("Bark Blamish Snail", SpellType.Earth, 30),
    Basilisk("Basilisk", SpellType.Earth, 40, true), // normal and monstrous
    Bat("Bat", SpellType.Air, 35),
    BearCub("Bear Cub", SpellType.Fire, 25),
    Berry("Berry", SpellType.Earth, 20),
    BerserkBarbarianSpirit("Berserk Barbarian Spirit", SpellType.Air, 25),
    BigWolf("Big Wolf", SpellType.Fire, 20),
    BigFrog("Big Frog", SpellType.Earth, 25),
    BillyGoat("Billy Goat", SpellType.Fire, 10),
    Bird("Bird", SpellType.Air, 25),
    BlackBear("Black Bear", SpellType.Fire, 30),
    BlackDemon("Black Demon", SpellType.Water, 40),
    BlackDragon("Black Dragon", SpellType.Water, 50, true), // Brutal, Baby, and normal black dragons all have the same %
    BlackJaguar("Black Jaguar", SpellType.Fire, 30),
    BlessedSpider("Blessed Spider", SpellType.Fire, 50),
    BloodBlamishSnail("Blood Blamish Snail", SpellType.Earth, 30),
    BloodMoon("Blood Moon", SpellType.Air, 15, -2),
    BloodJaguar("Blood Jaguar", SpellType.Earth, 15),
    BloodSpawn("Blood Spawn", SpellType.Earth, 50),
    BlueMoon("Blue Moon", SpellType.Air, 15, -5),
    BlueDragon("Blue Dragon", SpellType.Water, 50, true), // Brutal, Baby, and normal blue dragons all have the same %
    Boar("Boar", SpellType.Fire, 20),
    BodyGolem("Body Golem", SpellType.Earth, 35, 2),
    Bork("Bork", SpellType.Earth, 40),
    Bouncer("Bouncer", SpellType.Air, 15),
    BronzeDragon("Bronze Dragon", SpellType.Earth, 50),
    BrandaTheFireQueen("Branda the Fire Queen", SpellType.Water, 50),
    BruiseBlamishSnail("Bruise Blamish Snail", SpellType.Earth, 25),
    Bryophyta("Bryophyta", SpellType.Fire, 50),
    Callisto("Callisto", SpellType.Fire, 30),
    CaveBug("Cave Bug", SpellType.Fire, 50),
    CaveCrawler("Cave crawler", SpellType.Fire, 50),
    CaveGoblin("Cave Goblin", SpellType.Fire, 40, true), // normal, guard, and miner
    CaveHorror("Cave Horror", SpellType.Fire, 30),
    CaveKraken("Cave Kraken", SpellType.Earth, 50),
    CaveSlime("Cave Slime", SpellType.Earth, 50),
    Cerberus("Cerberus", SpellType.Water, 40),
    ChampionOfScabaras("Champion of Scabaras", SpellType.Fire, 35),
    ChaosElemental("Chaos Elemental", SpellType.Air, 50),
    ChaosGolem("Chaos Golem", SpellType.Earth, 35, 3),
    ChasmCrawler("Chasm Crawler", SpellType.Fire, 50),
    ChilledJelly("Chilled Jelly", SpellType.Fire, 50),
    ChokeDevil("Choke Devil", SpellType.Air, 25),
    Chronozon("Chronozon", SpellType.Water, 25),
    ColossalHydra("Colossal Hydra", SpellType.Earth, 40),
    CorporealBeast("Corporeal Beast", SpellType.Earth, 10),
    Crab("Crab", SpellType.Earth, 20),
    Crocodile("Crocodile", SpellType.Earth, 40),
    Cruor("Cruor", SpellType.NoType, 0, -2),
    CryptSpider("Crypt Spider", SpellType.Fire, 50),
    Dad("Dad", SpellType.Earth, 40),
    Dagannoth("Dagannoth", SpellType.Earth, 35, true), // normal, prime, rex, supreme, fledgling, mother, and spawn
    DarkAnkou("Dark Ankou", SpellType.Air, 20),
    DarkBeast("Dark Beast", SpellType.Earth, 60),
    Dawn("Dawn", SpellType.Earth, 70),
    DeadlyRedSpider("Deadly Red Spider", SpellType.Fire, 40),
    DeathWing("Death Wing", SpellType.Air, 20),
    Delrith("Delrith", SpellType.Water, 15),
    DemonicGorilla("Demonic Gorilla", SpellType.Water, 35),
    DeviantSpectre("Deviant Spectre", SpellType.Air, 30),
    Dharok("Dharok", SpellType.Air, 50, true),
    DireBat("Dire bat", SpellType.Air, 40),
    DireGryphon("Dire gryphon", SpellType.Air, 50),
    DontKnowWhat("Don't Know What", SpellType.Fire, 40),
    Doomion("Doomion", SpellType.Water, 25),
    Drake("Drake", SpellType.Water, 50, 2),
    DreadbornAraxyte("Dreadborn Araxyte", SpellType.Fire, 50),
    Dusk("Dusk", SpellType.Earth, 40, -1),
    DustDevil("Dust Devil", SpellType.Air, 35),
    EarthWarriorChampion("Earth Warrior Champion", SpellType.Earth, 50),
    EarthElemental("Earth Elemental", SpellType.Earth, 50),
    EarthWarrior("Earth Warrior", SpellType.Earth, 50),
    EarthenNagua("Earthen Nagua", SpellType.Air, 15, -4),
    EclipseMoon("Eclipse Moon", SpellType.Air, 15, 6),
    ElderCustodianStalker("Elder custodian stalker", SpellType.Fire, 30),
    EldricTheIceKing("Eldric the Ice King", SpellType.Fire, 50),
    ElidinisWarden("Elidinis' Warden", SpellType.Earth, 50),
    EliteBlackKnight("Elite Black Knight", SpellType.NoType, 0, 7),
    Elvarg("Elvarg", SpellType.Water, 30),
    EnormousTentacle("Enormous Tentacle", SpellType.Earth, 50),
    EnragedBarbarianSpirit("Enraged Barbarian Spirit", SpellType.Air, 15),
    Ent("Ent", SpellType.Fire, 40),
    EntranaFirebird("Entrana Firebird", SpellType.Water, 100),
    EvilSpirit("Evil Spirit", SpellType.Air, 30),
    Fareed("Fareed", SpellType.Water, 30),
    FerociousBarbarianSpirit("Ferocious Barbarian Spirit", SpellType.Air, 10),
    FeverSpider("Fever Spider", SpellType.Fire, 25),
    FireWarriorOfLesarkus("Fire Warrior of Lesarkus", SpellType.Water, 100),
    FireElemental("Fire Elemental", SpellType.Water, 100),
    FireGiant("Fire Giant", SpellType.Water, 100),
    FiyrShade("Fiyr Shade", SpellType.Air, 40, -3),
    Flambeed("Flambeed", SpellType.Water, 50),
    FlamingPyrelord("Flaming pyrelord", SpellType.Water, 100),
    FlawedGolem("Flawed Golem", SpellType.Earth, 35, 1),
    FleshCrawler("Flesh Crawler", SpellType.Fire, 20),
    FlightKilisa("Flight Kilisa", SpellType.Air, 30), // Needs icon
    FlockleaderGeerin("Flockleader Geerin", SpellType.Air, 30), // Needs icon
    Flower("Flower", SpellType.Fire, 100),
    ForgottenSoul("Forgotten Soul", SpellType.Air, 50),
    Fox("Fox", SpellType.Fire, 25),
    FrenziedIceTroll("Frenzied Ice Troll", SpellType.Fire, 50, true), // female, grunt, male, and runt
    Frog("Frog", SpellType.Earth, 40),
    Frogeel("Frogeel", SpellType.Earth, 40, -2),
    FrostCrab("Frost Crab", SpellType.Fire, 100),
    FrostDragon("Frost dragon", SpellType.Fire, 100),
    FrostNagua("Frost Nagua",SpellType.Fire, 50, -4),
    Fumus("Fumus", SpellType.NoType, 0, -2),
    Galvek("Galvek", SpellType.Water, 40),
    Gargoyle("Gargoyle", SpellType.Earth, 40, true, -2), // normal and marble
    GeneralGraardor("General Graardor", SpellType.Earth, 40),
    Ghast("Ghast", SpellType.Air, 25),
    Ghost("Ghost", SpellType.Air, 50),
    GiantBat("Giant Bat", SpellType.Air, 10),
    GiantCryptSpider("Giant Crypt Spider", SpellType.Fire, 40),
    GiantFrog("Giant Frog", SpellType.Earth, 25),
    GiantLobster("Giant Lobster", SpellType.Earth, 25),
    GiantMole("Giant Mole", SpellType.Earth, 50),
    GiantMosquito("Giant Mosquito", SpellType.Fire, 40),
    GiantRoc("Giant Roc", SpellType.Air, 35),
    GiantRockCrab("Giant Rock Crab", SpellType.Earth, 20),
    GiantRockslug("Giant Rockslug", SpellType.Earth, 20, 2),
    GiantScarab("Giant Scarab", SpellType.Fire, 50),
    GiantSkeleton("Giant Skeleton", SpellType.Earth, 40),
    GiantSnail("Giant Snail", SpellType.Earth, 45),
    GiantSpider("Giant spider", SpellType.Fire, 50),
    Glacies("Glacies", SpellType.NoType, 0, -2),
    Glod("Glod", SpellType.Earth, 50),
    Gorak("Gorak", SpellType.NoType, 0, -2),
    GraveScorpion("Grave Scorpion", SpellType.Fire, 40),
    GreaterDemon("Greater Demon", SpellType.Water, 40),
    GreaterSkeletonHellhound("Greater Skeleton Hellhound", SpellType.Earth, 40),
    GreatOlm("Great Olm", SpellType.Earth, 50),
    GreenDragon("Green Dragon", SpellType.Water, 50, true), // Brutal, Baby, and normal blue dragons all have the same %
    GrimyLizard("Grimy Lizard", SpellType.NoType, 0, -2),
    GrizzlyBear("Grizzly Bear", SpellType.Fire, 20), // normal and cub
    GrizzlyBearCub("Grizzly bear cub", SpellType.Fire, 20),
    Gryphon("Gryphon", SpellType.Air, 50),
    GuardianDrake("Guardian Drake", SpellType.Water, 50, 3),
    Guthan("Guthan", SpellType.Air, 50, true),
    HarpieBugSwarm("Harpie Bug Swarm", SpellType.Fire, 50),
    Hellhound("Hellhound", SpellType.Water, 50),
    Hespori("Hespori", SpellType.Fire, 100),
    HillGiant("Hill Giant", SpellType.Earth, 25),
    Holthion("Holthion", SpellType.Water, 10),
    HopelessCreature("Hopeless Creature", SpellType.Fire, 40),
    Hueycoatl("Hueycoatl", SpellType.Earth, 60, true), // all entities of it
    HugeSpider("Huge Spider", SpellType.Fire, 40),
    Hydra("Hydra", SpellType.Earth, 40),
    IceDemon("Ice Demon", SpellType.Fire, 150),
    IceElemental("Ice elemental", SpellType.Fire, 100),
    Icefiend("Icefiend", SpellType.Fire, 100),
    IceGiant("Ice Giant", SpellType.Fire, 100),
    Icelord("Icelord", SpellType.Fire, 30),
    IceQueen("Ice Queen", SpellType.Fire, 100),
    IceSpider("Ice Spider", SpellType.Fire, 100),
    IceTroll("Ice Troll", SpellType.Fire, 100, true), // Ordinary, Male, and Female all have the same %
    IceTrollGrunt("Ice Troll Grunt", SpellType.Fire, 35),
    IceTrollKing("Ice Troll King", SpellType.Fire, 35),
    IceTrollRunt("Ice Troll Runt", SpellType.Fire, 50),
    IceWarrior("Ice Warrior", SpellType.Fire, 100),
    IceWolf("Ice Wolf", SpellType.Fire, 20),
    Imp("Imp", SpellType.Water, 10),
    ImpChampion("Imp Champion", SpellType.Water, 10),
    InfernalPyrelord("Infernal Pyrelord", SpellType.Water, 10),
    IronDragon("Iron Dragon", SpellType.Earth, 50),
    IrvigSenay("Irvig Senay", SpellType.Air, 15),
    Jackal("Jackal", SpellType.Fire, 10),
    Jaguar("Jaguar", SpellType.Fire, 10), // don't use partial
    JaguarCub("Jaguar Cub", SpellType.Fire, 10),
    Jal("Jal-", SpellType.Water, 40, true), // multiple tzhaar jal-
    JalTokJad("JalTok-Jad", SpellType.Water, 40),
    Jelly("Jelly", SpellType.Earth, 35),
    Jogre("Jogre", SpellType.Earth, 25), // normal and champion
    Jormungand("Jormungand", SpellType.Earth, 40, true), // The Jormungand
    JudgeOfYama("Judge of Yama", SpellType.Water, 15),
    JungleDemon("Jungle Demon", SpellType.Fire, 25),
    JungleWolf("Jungle Wolf", SpellType.Fire, 15),
    JungleHorror("Jungle Horror", SpellType.Fire, 25),
    JungleSpider("Jungle Spider", SpellType.Fire, 40, -2),
    JuvenileCustodianStalker("Juvenile custodian stalker", SpellType.Fire, 30),
    Kalphite("Kalphite", SpellType.Fire, 40, true), // normal, Guardian, QUeen, Soldier, and Worker
    Kalrag("Kalrag", SpellType.Fire, 10),
    Kamil("Kamil", SpellType.Fire, 10),
    Karamel("Karamel", SpellType.Fire, 40),
    Karil("Karil", SpellType.Air, 50, true),
    KarilTheTainted("Karil the Tainted", SpellType.Air, 50, -2),
    KasondeTheCraven("Kasonde the Craven", SpellType.Air, 15),
    KetZek("Ket-Zek", SpellType.Water, 40),
    KetlaTheUnworthy("Ketla the Unworthy", SpellType.Air, 15),
    Kephri("Kephri", SpellType.Fire, 35), // 40% with shield, not possible to calculate
    Killerwatt("Killerwatt", SpellType.Air, 60),
    KingBlackDragon("King Black Dragon", SpellType.Water, 50),
    KingSandCrab("King Sand Crab", SpellType.Earth, 15),
    KingScorpion("King Scorpion", SpellType.Fire, 20),
    Kob("Kob", SpellType.Earth, 20),
    Kraka("Kraka", SpellType.Earth, 20),
    Kraken("Kraken", SpellType.Earth, 50),
    Kreearra("Kree'arra", SpellType.Air, 30),
    KrilTsutsaroth("K'ril Tsutsaroth", SpellType.Water, 30),
    LargeMosquito("Large Mosquito", SpellType.Fire, 40),
    LavaBeast("Lava beast", SpellType.Water, 100),
    LavaDragon("Lava Dragon", SpellType.Water, 50),
    LavaStrykewyrm("Lava Strykewyrm", SpellType.Water, 50),
    LesserDemon("Lesser Demon", SpellType.Water, 40),
    LesserDemonChampion("Lesser Demon Champion", SpellType.Water, 40),
    LizardmanBrute("Lizardman brute", SpellType.NoType, 0, -2),
    LoarShade("Loar Shade", SpellType.Air, 40, -3),
    Lobstrosity("Lobstrosity", SpellType.Earth, 25),
    Locust("Locust", SpellType.Fire, 40),
    LocustRider("Locust Rider", SpellType.Fire, 40, -2),
    LongTailedWyvern("Long-tailed Wyvern", SpellType.Air, 25),
    MagmaStrykewyrm("Magma strykewyrm", SpellType.Water, 50),
    MatureCustodianStalker("Mature custodian stalker", SpellType.Fire, 30),
    MetzliTeokanOfRanul("Metzli, Teokan of Ranul", SpellType.Earth, 30),
    MindGolem("Mind Golem", SpellType.Earth, 35, 1),
    MirrorbackAraxyte("Mirrorback Araxyte", SpellType.Fire, 50),
    MithrilDragon("Mithril Dragon", SpellType.Earth, 50),
    Mogre("Mogre", SpellType.Earth, 20),
    Moia("Moia", SpellType.Air, 80),
    Molanisk("Molanisk", SpellType.Earth, 60),
    MonkeyZombie("Monkey Zombie", SpellType.Fire, 50),
    MosquitoSwarm("Mosquito swarm", SpellType.Fire, 45),
    MossGiant("Moss Giant", SpellType.Fire, 50),
    MossGuardian("Moss Guardian", SpellType.Fire, 50),
    Mother("Mother", SpellType.Fire, 20),
    MountainTroll("Mountain Troll", SpellType.Earth, 50),
    Mudskipper("Mudskipper", SpellType.Earth, 20),
    Mummy("Mummy", SpellType.Fire, 50),
    MutatedTerrorbird("Mutated Terrorbird", SpellType.NoType, 0, -2),
    MutatedTortoise("Mutated Tortoise", SpellType.Earth, 25, 1),
    Muttadile("Muttadile", SpellType.Earth, 40),
    MyreBlamishSnail("Myre Blamish Snail", SpellType.Earth, 25),
    Nazastarool("Nazastarool", SpellType.Air, 20),
    Nezikchened("Nezikchened", SpellType.Water, 10),
    NuclearSmokeDevil("Nuclear Smoke Devil", SpellType.Air, 20),
    Nylocas("Nylocas", SpellType.Fire, 15, true), // Athanatos, Hagios, Ischyros, Matomenos, Prikipas, Toxobolos, and Vasilias
    Obor("Obor", SpellType.Earth, 20),
    Ocelot("Ocelot", SpellType.Fire, 10),
    OchreBlamishSnail("Ochre Blamish Snail", SpellType.Earth, 25),
    Ogre("Ogre", SpellType.Earth, 20, true), // normal, chieftain, shaman, ogress and reanimated
    Ork("Ork", SpellType.Earth, 15),
    Othainian("Othainian", SpellType.Water, 10),
    OtherworldlyBeing("Otherworldly Being", SpellType.Air, 35, -2),
    Parasite("Parasite", SpellType.Fire, 50),
    PeeHat("Pee Hat", SpellType.Earth, 20),
    Penguin("Penguin", SpellType.Fire, 25),
    PerstenTheDeceitful("Persten the Deceitful", SpellType.Air, 15),
    PhantomMuspah("Phantom Muspah", SpellType.Air, 65),
    PhrinShade("Phrin Shade", SpellType.Air, 40, -3),
    PoisonSpider("Poison Spider", SpellType.Fire, 40),
    Porcupine("Porcupine", SpellType.Fire, 20),
    Portal("Portal", SpellType.NoType, 0, -2),
    PygmyKraken("Pygmy kraken", SpellType.Earth, 50),
    Pyrefiend("Pyrefiend", SpellType.Water, 100),
    Pyrelord("Pyrelord", SpellType.Water, 100),
    Rabbit("Rabbit", SpellType.Fire, 20),
    Ram("Ram", SpellType.Fire, 20),
    RanalphDevere("Ranalph Devere", SpellType.Air, 10),
    ReanimatedAviansie("Reanimated Aviansie", SpellType.Air, 30),
    ReanimatedBear("Reanimated Bear", SpellType.Fire, 20),
    ReanimatedDagannoth("Reanimated Dagannoth", SpellType.Earth, 50),
    ReanimatedDemon("Reanimated Demon", SpellType.Water, 10),
    ReanimatedDragon("Reanimated Dragon", SpellType.Water, 50),
    ReanimatedGiant("Reanimated Giant", SpellType.Earth, 20),
    ReanimatedGoblin("Reanimated Goblin", SpellType.Fire, 10),
    ReanimatedHellhound("Reanimated Hellhound", SpellType.Water, 10),
    ReanimatedImp("Reanimated Imp", SpellType.Water, 10),
    ReanimatedKalphite("Reanimated Kalphite", SpellType.Fire, 50),
    RedDragon("Red Dragon", SpellType.Water, 50, true), // Brutal, Baby, and normal black dragons all have the same %
    RepugnantSpectre("Repugnant Spectre", SpellType.Air, 40),
    Revenant("Revenant", SpellType.Air, 30, true), // cyclops, dark beast, demon, dragon, goblin, hellhound, hobgoblin, imp, knight, ork, pyrefiend
    Riyl("Riyl", SpellType.Air, 40, true), // Shade and shadow
    RiylShade("Riyl Shade", SpellType.Air, 40, -3),
    RockCrab("Rock Crab", SpellType.Earth, 20),
    RockGolem("Rock Golem", SpellType.Earth, 40, 2),
    RockLobster("Rock Lobster", SpellType.Earth, 25),
    Rockslug("Rockslug", SpellType.Earth, 25),
    RuneDragon("Rune dragon", SpellType.Earth, 50),
    RuniteGolem("Runite Golem", SpellType.Earth, 60, 4),
    RupturaAraxyte("Ruptura Araxyte", SpellType.Fire, 50),
    SanTojalon("San Tojalon", SpellType.Air, 10),
    SandCrab("Sand Crab", SpellType.Earth, 20),
    SandSnake("Sand Snake", SpellType.Earth, 40),
    Sanguidae("Sanguidae", SpellType.Fire, 40),
    Sarachnis("Sarachnis", SpellType.Fire, 40),
    Scarab("Scarab", SpellType.Fire, 40),
    ScarabMage("Scarab Mage", SpellType.Fire, 50),
    ScarabSwarm("Scarab Swarm", SpellType.Fire, 40),
    Scarabs("Scarabs", SpellType.Fire, 50),
    ScarredBlackDemon("Scarred black demon", SpellType.Water, 40),
    ScarredGreaterDemon("Scarred greater demon", SpellType.Water, 40),
    ScarredHellhound("Scarred hellhound", SpellType.Water, 50),
    ScarredImp("Scarred Imp", SpellType.Water, 10),
    ScarredLesserDemon("Scarred Lesser Demon", SpellType.Water, 40),
    Scorpia("Scorpia", SpellType.Fire, 35),
    ScorpiaSGuardian("Scorpia's guardian", SpellType.Fire, 35),
    ScorpiaSOffspring("Scorpia's offspring", SpellType.Fire, 35),
    Scorpion("Scorpion", SpellType.Fire, 25, true), // normal, Khazard, Pit, Poison, and reanimated
    ScreamingBanshee("Screaming Banshee", SpellType.Air, 40),
    ScreamingTwistedBanshee("Screaming Twisted Banshee", SpellType.Air, 40),
    SeaSnake("Sea Snake", SpellType.Earth, 20, true), // Giant, young and Hatchling
    SeaTroll("Sea Troll", SpellType.Earth, 15, true), // normal and queen
    Seagull("Seagull", SpellType.Air, 20),
    Shade("Shade", SpellType.Air, 40, -3),
    Shadow("Shadow", SpellType.Air, 40),
    ShadowWyrm("Shadow Wyrm", SpellType.Earth, 50, 2),
    ShadowSpider("Shadow Spider", SpellType.Fire, 40),
    ShellbaneGryphon("Shellbane gryphon", SpellType.Air, 50),
    SkeletalHellhound("Skeletal Hellhound", SpellType.Earth, 35),
    SkeletalWyvern("Skeletal Wyvern", SpellType.Fire, 25),
    Skeleton("Skeleton", SpellType.Earth, 40, true), // normal, mage, brute, fremennik, guard, heavy, hero, thug, champion
    SkeletonHeavy("Skeleton heavy", SpellType.Earth, 35, 1),
    SkeletonHellhound("Skeleton Hellhound", SpellType.Earth, 30),
    SkeletonHero("Skeleton hero", SpellType.Earth, 35, 1),
    SkeletonThug("Skeleton thug", SpellType.Earth, 35, 1),
    SkeletonWarlord("Skeleton Warlord", SpellType.Earth, 35),
    Skotizo("Skotizo", SpellType.Water, 40),
    Slagilith("Slagilith", SpellType.Earth, 40),
    SmallScarab("Small Scarab", SpellType.Fire, 50),
    SmokeDevil("Smoke Devil", SpellType.Air, 30),
    SoldierScarab("Soldier scarab", SpellType.Fire, 50),
    Spider("Spider", SpellType.Fire, 50),
    Spidine("Spidine", SpellType.Fire, 25),
    Spindel("Spindel", SpellType.Fire, 25, true), // Spindel and spiderlings
    SpinedKraken("Spined kraken", SpellType.Earth, 50),
    SpiritualMage("Spiritual Mage", SpellType.Air, 30),
    SpiritualRanger("Spiritual Ranger", SpellType.Air, 30),
    SpiritualWarrior("Spiritual Warrior", SpellType.Air, 30),
    SpittingWyvern("Spitting Wyvern", SpellType.Air, 25),
    SpittingScarab("Spitting scarab", SpellType.Fire, 50),
    SteelDragon("Steel dragon", SpellType.Earth, 50),
    StoneGuardian("Stone Guardian", SpellType.Earth, 40),
    StrangeCreature("Strange creature", SpellType.Fire, 30),
    Strangled("Strangled", SpellType.Fire, 50),
    StrangledBear("Strangled Bear", SpellType.Fire, 60),
    StrangledBoar("Strangled Boar", SpellType.Fire, 60),
    StrangledLynx("Strangled Lynx", SpellType.Fire, 60),
    SulphurLizard("Sulphur Lizard", SpellType.NoType, 0, -1),
    SulphurNagua("Sulphur Nagua", SpellType.Air, 15, -4),
    SummonedZombie("Summoned Zombie", SpellType.Fire, 50),
    Suqah("Suqah", SpellType.Earth, 20),
    SwampCrab("Swamp Crab", SpellType.Earth, 35),
    SwampFrog("Swamp Frog", SpellType.Earth, 20),
    TalonedWyvern("Taloned Wyvern", SpellType.Air, 35),
    Tanglefoot("Tanglefoot", SpellType.Fire, 35),
    Tekton("Tekton", SpellType.Water, 20),
    TempleSpider("Temple Spider", SpellType.Fire, 40),
    TheDraugen("The Draugen", SpellType.Air, 50),
    TheForsakenAssassin("The Forsaken Assassin", SpellType.Air, 15),
    TheSmorks("The Smorks", SpellType.Earth, 85, 2),
    ThermonuclearSmokeDevil("Thermonuclear Smoke Devil", SpellType.Air, 20),
    TokXil("Tok-Xil", SpellType.Water, 40),
    Torag("Torag", SpellType.Air, 50, true),
    TormentedDemon("Tormented Demon", SpellType.Water, 30),
    TormentedSoul("Tormented Soul", SpellType.Air, 20),
    Tortoise("Tortoise", SpellType.Earth, 20),
    Totem("Totem", SpellType.Fire, 70),
    TrappedSoul("Trapped Soul", SpellType.Air, 15),
    TreeSpirit("Tree Spirit", SpellType.Fire, 35),
    TreusDayth("Treus Dayth", SpellType.Air, 25),
    Troll("Troll", SpellType.Earth, 20, true), // normal, reanimated, river, thrower, general, spectator
    TstanonKarlak("Tstanon Karlak", SpellType.Water, 40),
    TumekensWarden("Tumeken's Warden", SpellType.Earth, 50),
    TwistedBanshee("Twisted Banshee", SpellType.Air, 25),
    TzKek("Tz-Kek", SpellType.Water, 40),
    TzKih("Tz-Kih", SpellType.Water, 40),
    TzHaar("TzHaar-", SpellType.Water, 40, true), // reanimated, Hur, Ket, Mej, Xil
    TzKal("TzKal-", SpellType.Water, 40, true), // Zuk
    TzTokJad("TzTok-Jad", SpellType.Water, 40),
    TzTokJadRek("TzTok-Jad-Rek", SpellType.Water, 40),
    Umbra("Umbra", SpellType.NoType, 0, -2),
    UnboundJalTokJad("Unbound JalTok-Jad", SpellType.Water, 40),
    Undead("Undead", SpellType.Fire, 50, true), // druid, lumberjack, zealot, chicken, cow, one
    UriumShade("Urium Shade", SpellType.Air, 40, -3),
    VampyreKraken("Vampyre kraken", SpellType.Earth, 50),
    VampyreSnail("Vampyre Snail", SpellType.Earth, 5),
    Vardorvis("Vardorvis", SpellType.Fire, 35),
    VeiledKraken("Veiled kraken", SpellType.NoType, 0, 15),
    Venenatis("Venenatis", SpellType.Fire, 40, true), // venenatis and spiderlings
    Verac("Verac", SpellType.Air, 50, true),
    // verzik vitur phase 3?
    VerzikVitur("Verzik Vitur", SpellType.Fire, 50),
    VespineSoldier("Vespine Soldier", SpellType.Fire, 40),
    Vespula("Vespula", SpellType.Fire, 50),
    VitreousChilledJelly("Vitreous Chilled Jelly", SpellType.Earth, 30),
    VitreousJelly("Vitreous Jelly", SpellType.Earth, 30),
    Vorkath("Vorkath", SpellType.Fire, 40),
    Vulture("Vulture", SpellType.Air, 20),
    Wallasalki("Wallasalki", SpellType.Earth, 30),
    WarpedJelly("Warped Jelly", SpellType.Earth, 30, true), // normal and Vitreous
    WarpedTortoise("Warped Tortoise", SpellType.Earth, 20),
    WaterElemental("Water Elemental", SpellType.Earth, 50),
    Waterfiend("Waterfiend", SpellType.Earth, 100),
    TheWhisperer("The Whisperer", SpellType.Earth, 60),
    WebbedWingedCrow("Webbed-winged Crow", SpellType.Air, 25),
    WhiteWolf("White wolf", SpellType.Fire, 25),
    WingmanSkree("Wingman Skree", SpellType.Air, 30),
    Wolf("Wolf", SpellType.Fire, 25), // dont use partial
    Wyrm("Wyrm", SpellType.Earth, 50, 1),
    Wyrmling("Wyrmling", SpellType.Earth, 50),
    Xarpus("Xarpus", SpellType.Air, 50),
    Xarth("Xarth", SpellType.Water, 85),
    Yama("Yama", SpellType.Water, 50),
    YtHurKot("Yt-HurKot", SpellType.Water, 40),
    YtMejKot("Yt-MejKot", SpellType.Water, 40),
    ZaklnGritch("Zakl'n Gritch", SpellType.Water, 40),
    Zebak("Zebak", SpellType.Earth, 40),
    ZemouregalSummon("Zemouregal Summon", SpellType.NoType, 0, -3),
    Zombie("Zombie", SpellType.Fire, 50, true), // normal, pirate, rat, swab, champion
    ZombifiedSpawn("Zombified Spawn", SpellType.Fire, 50),
    Zulrah("Zulrah", SpellType.Fire, 50),
    Zygomite("Zygomite", SpellType.Fire, 40);

    @Getter
    private final String NPCName;
    @Getter
    private final SpellType elementalWeakness;
    @Getter
    private final int weaknessPercent;
    @Getter
    private final boolean allowPartialSearch;
    @Getter
    private final int flatArmour;

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

    NPCTypeWeakness(String NPCName, SpellType elementalWeakness, int weaknessPercent)
    {
        this(NPCName, elementalWeakness, weaknessPercent, false, 0);
    }

    NPCTypeWeakness(String NPCName, SpellType elementalWeakness, int weaknessPercent, boolean allowPartialSearch)
    {
        this(NPCName, elementalWeakness, weaknessPercent, allowPartialSearch, 0);
    }

    NPCTypeWeakness(String NPCName, SpellType elementalWeakness, int weaknessPercent, int flatArmour)
    {
        this(NPCName, elementalWeakness, weaknessPercent, false, flatArmour);
    }

    NPCTypeWeakness(String NPCName, SpellType elementalWeakness, int weaknessPercent, boolean allowPartialSearch, int flatArmour)
    {
        this.NPCName = NPCName;
        this.elementalWeakness = elementalWeakness;
        this.weaknessPercent = weaknessPercent;
        this.allowPartialSearch = allowPartialSearch;
        this.flatArmour = flatArmour;
    }
}
