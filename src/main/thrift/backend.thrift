
namespace java os.faproj.api

service BackendService {
    void auth(1: string uid)
    bool login(1: string uid)
    // bool logout(1: string uid)
}
