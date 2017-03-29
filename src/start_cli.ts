#!/usr/bin/env node

import start_base = require("./cli/cmd-start/start_base");
import DefineEnv = require("./cli/default-define/define_env");
import argv = require('argv');


let defEnv = DefineEnv.upEnv();
defEnv.pathStart = __dirname;

defEnv.pathCwd = process.cwd();


let args = argv.option(
    [
        {
            name: 'config',
            type: 'boolean',
            description: 'init project with one config.json file'
        },
        {
            name: 'install',
            type: 'boolean',
            description: 'install npm files ,exec after init'
        }
        ,
        {
            name: 'force',
            type: 'boolean',
            description: 'force overwrite'
        }
    ]
).run();


defEnv.argsConfig = args.options.init;
defEnv.argsInstall = args.options.install;
defEnv.argsForce = args.options.force;


start_base.initStart(defEnv);

