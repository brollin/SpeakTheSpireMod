package saythespiremod;

import io.javalin.Javalin;

public class SayTheSpireServer {
    private static final int PORT = 10463;

    public static void start() {
        Javalin.create(config -> {
            // config.jetty
        }).get("/monsters", ctx -> ctx.result(SayTheSpireApi.getMonsterData())).start(PORT);
    }
}
