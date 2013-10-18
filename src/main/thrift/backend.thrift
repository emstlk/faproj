
namespace java os.faproj.api

service BackendService {
    i64 getCounter()
    i64 incrementCounter()
    void saveString(1: string msg)
    string getString()
}
