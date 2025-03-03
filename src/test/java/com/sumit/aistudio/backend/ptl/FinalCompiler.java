package com.sumit.aistudio.backend.ptl;

import com.sumit.aistudio.backend.agents.OllamaAgent;
import com.sumit.aistudio.backend.linker.LinkInfoService;
import com.sumit.aistudio.backend.prompt.PromptService;
import com.sumit.aistudio.backend.ptl.nodes.GenInfoNode;
import com.sumit.aistudio.backend.ptl.nodes.GeneratedNode;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.exit;

@SpringBootApplication
public class FinalCompiler {
	@Autowired LinkInfoService linkInfoService;
	@Autowired
	PromptLinker linkerInfo;
	@Autowired
	PromptService promptService;
	@Autowired
    OllamaAgent agent;
	static final Map<String,Object > arguments = new HashMap<>();
	public static void main(String[] args) throws ParseException {

		Options options = new Options();
		options.addOption(new Option("b", "base", true, "base dir"));

		options.addOption(new Option("f", "file", true, "prompt file to compile."));
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		if (cmd.hasOption("base") && cmd.hasOption("file")) {
			arguments.put("base",cmd.getOptionValue("base"));
			arguments.put("file",cmd.getOptionValue("file"));
		}else{
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("final compiler", options);
			exit(1);
		}
		SpringApplication.run(FinalCompiler.class, args);
	}
	String file = null;
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() throws IOException {
		System.out.println("hello world, I have just started up");
		System.out.printf("Going to compile: "+arguments.get("file"));
		PromptFileParser tester = new PromptFileParser();

		PromtCompilerGenerator gen = new PromtCompilerGenerator();

		if(arguments.get("file")!=null) {
			String base = arguments.get("base").toString();
			String[] files = StringUtils.split(arguments.get("file").toString(), ",");
			for (String file : files) {
				String fileContent = FileUtils.readFileToString(new File(base + file));
				linkerInfo.addPromptOutput("current_file",fileContent);
				GenInfoNode node = tester.parseFileFinal(base + file, linkerInfo);
				System.out.println(node);
				GeneratedNode rootGen = gen.generate(node, linkerInfo,promptService,agent);
				System.out.println(rootGen.getInfo());
				FileUtils.write(new File(base + file), rootGen.getInfo());
			}
			linkInfoService.saveLinkInfo(linkerInfo);
		}



		//System.exit(0);
	}

}
