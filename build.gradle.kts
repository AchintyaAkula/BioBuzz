plugins {
    id("dev.frozenmilk.teamcode") version "12.0.0-1.2.2"
    id("dev.frozenmilk.sinister.sloth.load") version "0.3.2"
}

ftc {
    kotlin()
}

dependencies {
    implementation("dev.frozenmilk.sinister:Sloth:0.3.2")

    implementation("org.firstinspires.ftc:Inspection:12.0.0")
    implementation("org.firstinspires.ftc:Blocks:12.0.0")
    //noinspection Aligned16KB
    implementation("org.firstinspires.ftc:RobotCore:12.0.0")
    implementation("org.firstinspires.ftc:RobotServer:12.0.0")
    implementation("org.firstinspires.ftc:OnBotJava:12.0.0")
    implementation("org.firstinspires.ftc:Hardware:12.0.0")
    implementation("org.firstinspires.ftc:FtcCommon:12.0.0")
    implementation("org.firstinspires.ftc:Vision:12.0.0")
    //noinspection GradleDependency
    implementation("androidx.appcompat:appcompat:1.2.0")

    implementation("com.pedropathing:revhub:3.0.1")
    implementation("com.pedropathing:tuning:1.0.1")

    implementation("com.pedropathing.ivy:pedro:1.1.1")
}
