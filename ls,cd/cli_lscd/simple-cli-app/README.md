# simple-cli-app
Practice project for SPbAU Software Design course

Project description can be found [here](https://drive.google.com/file/d/123esKS-QhQxkbQhpobWpvW0HHIRV2jCU/view).

## Task 1 

Current status: not even close to finish.

Few words about (supposed) architecture.

Tasks are represented by `Task` interface; if you want to add more tasks, you should implement it and add to Map that is passed to `Interpreter`

Class `Interpreter` should control all parts of the application. 

* use `Tokenizer` to split strings and create `TaskInfo`s from them
* create `Workflow` to create a pipe from tasks
    * internally `Workflow` creates new instances of `ShellProcess` and connects them with piped streams
    * on `execute` it invokes them sequentially, one after another 
* pass required streams into it's `execute` method

No substitution implemented yet.