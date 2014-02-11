
namespace java os.faproj.api

enum BattleResult {
    UNKNOWN = 0
    WIN = 1
    FAIL = 2
}

struct Creature {
    1: i32 id
    2: string uid
    3: i32 attack
    4: i32 defence
    5: i32 accuracy
    6: i32 evasion
    7: i32 life
    8: i32 initiative
}

struct Battle {
    1: Creature attacker
    2: Creature defender
    3: BattleResult result
}

service BackendService {
    void auth(1: string uid)
    bool login(1: string uid)
    // bool logout(1: string uid)

    string getLastConfig(1: byte version)

    void endBattle(1: Battle battle)
    i32 getLifePoints(1: string uid, 2: i32 creatureId)
}
