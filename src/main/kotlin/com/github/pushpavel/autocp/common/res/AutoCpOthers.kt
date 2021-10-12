package com.github.pushpavel.autocp.common.res

import com.github.pushpavel.autocp.build.DefaultLangData

object AutoCpOthers {
    val competitiveCompanionPorts = listOf(
        27121, // Competitive Programming Helper
        10045, // CP Editor
        10043, // Caide and AI Virtual Assistant
        10042, // acmX
        6174, // Mind Sport
        4244, // Hightail
        1327, // cpbooster
    )

    const val problemGatheringTimeoutMillis = 30000 // 30 sec

    val defaultLangs = listOf(
        DefaultLangData(
            "c",
            listOf(
                Pair("gcc @in -o \"./a.exe\"", "\"\$dir/a.exe\""),
                Pair("clang @in -o \"./a.exe\"", "\"\$dir/a.exe\""),
            ),
        ),
        DefaultLangData(
            "cpp",
            listOf(
                Pair("g++ @in -o \"./a.exe\" -std=c++17", "\"\$dir/a.exe\""),
                Pair("clang++ @in -o \"./a.exe\" -std=c++17", "\"\$dir/a.exe\""),
            ),
        ),
        DefaultLangData(
            "java",
            listOf(
                Pair("javac @in -d @dir", "java Main"),
            ),
        ),
        DefaultLangData(
            "py",
            listOf(
                Pair(null, "python @in"),
            ),
        ),
        DefaultLangData(
            "kt",
            listOf(
                Pair("kotlinc @in -include-runtime -d solution.jar", "java -jar solution.jar"),
            ),
        ),
        DefaultLangData(
            "rs",
            listOf(
                Pair("rustc @in -o a.exe", "\"\$dir/a.exe\""),
            ),
        ),
        DefaultLangData(
            "js",
            listOf(
                Pair(null, "d8 @in"),
                Pair(null, "node @in"),
            ),
        ),
    )
}