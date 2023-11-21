# kcoder_wiki
Wiki based on the ktt wiki I'm developing

# Getting Started
You can start the wiki either by calling ```Main.main()``` or by calling:

```
Main.registerMacro(anyMacroYouHave)
Main.setup()
Main.startup()
```


# Building
You can use the ```gradlew fatjar``` gradle task to build the project into an executable jar

# Plugins
## umlGen
Creates UML using the grucd (grand unified code documentor) library.  Example usages:

```
{@umlGen path=/path/to/code}
```

This will render as UML for any supported code at the given path.