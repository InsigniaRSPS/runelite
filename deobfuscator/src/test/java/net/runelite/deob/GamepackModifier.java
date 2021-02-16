package net.runelite.deob;

import com.google.common.base.Stopwatch;
import net.runelite.asm.ClassGroup;
import net.runelite.deob.deobfuscators.transformers.ConstantStringTransformer;
import net.runelite.deob.deobfuscators.transformers.RsaTransformer;
import net.runelite.deob.util.JarUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class GamepackModifier {

    private static final Logger logger = LoggerFactory.getLogger(GamepackModifier.class);

    public static void main(String[] args) throws IOException {
        if (args == null || args.length < 2) {
            System.err.println("Syntax: input_jar output_jar");
            System.exit(-1);
        }

        Stopwatch stopwatch = Stopwatch.createStarted();
        ClassGroup group = JarUtil.loadJar(new File(args[0]));

        new RsaTransformer().transform(group);
        new ConstantStringTransformer().transform(group);

        JarUtil.saveJar(group, new File(args[1]));

        stopwatch.stop();
        logger.info("Done in {}", stopwatch);
    }

}
