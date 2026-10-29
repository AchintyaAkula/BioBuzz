plugins {
    id("dev.frozenmilk.teamcode") version "12.0.0-1.2.2"
    id("dev.frozenmilk.sinister.sloth.load") version "0.3.2"
    id("com.diffplug.spotless") version "8.10.4"
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
    implementation("com.qualcomm.ftcrobotcontroller:FtcRobotController:12.0.0")
    //noinspection GradleDependency
    implementation("androidx.appcompat:appcompat:1.2.0")

    implementation("com.pedropathing:revhub:3.0.2")
    implementation("com.pedropathing:tuning:1.0.1")

    implementation("com.acmerobotics.slothboard:dashboard:0.3.2+0.6.0")

    // add solverslib
    implementation("org.solverslib:core:0.3.5")
}

spotless {
    kotlinGradle {
        target("**/*.gradle.kts")

        ktlint()
            .editorConfigOverride(kotlinFormattingConfiguration)
    }
    java {
        target("src/main/java/**/*.java", "src/test/java/**/*.java")

        palantirJavaFormat()
        removeUnusedImports()
        importOrder("java", "javax", "jakarta")
        trimTrailingWhitespace()
        endWithNewline()
        formatAnnotations()
    }
}

val kotlinFormattingConfiguration: Map<String, String> = mapOf(
    "kotlin_code_style" to "official",
    "indent_size" to "4",
    "max_line_length" to "120",
    "insert_final_newline" to "true",
    "trim_trailing_whitespace" to "true",
)