#     L'archive bin/gui.jar contient les classes de l'interface graphique
#
# Compilation:
#  Options de javac:
#   -d : repertoire dans lequel sont places les .class compiles
#   -classpath : repertoire dans lequel sont cherches les .class deja compiles
#   -sourcepath : repertoire dans lequel sont cherches les .java (dependances)

numberCouples?=6
numberGames?=1

all: testFourCouples testFirstPossible testLocalSearch

testFourCouples:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/tests/TestFourCouples.java

testLocalSearch:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/tests/TestLocalSearch.java

testFirstPossible:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/tests/TestFirstPossible.java

testRandomPossible:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/tests/TestRandomPossible.java

testMeanLocalSearch:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/tests/TestMeanLocalSearch.java

testMeanRandomPossible:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/tests/TestMeanRandomPossible.java


# Execution:
# on peut taper directement la ligne de commande :
#   > java -classpath bin:bin/gui.jar TestInvader
# ou bien lancer l'execution en passant par ce Makefile:
#   > make exeInvader
exeFourCouples:
	java -classpath bin:bin/gui.jar TestFourCouples

exeFirstPossible:
	java -classpath bin:bin/gui.jar TestFirstPossible $(numberCouples)

exeLocalSearch:
	java -classpath bin:bin/gui.jar TestLocalSearch $(numberCouples)

exeRandomPossible:
	java -classpath bin:bin/gui.jar TestRandomPossible $(numberCouples)

exeMeanLocalSearch:
	java -classpath bin:bin/gui.jar TestMeanLocalSearch $(numberCouples) $(numberGames)

exeMeanRandomPossible:
	java -classpath bin:bin/gui.jar TestMeanRandomPossible $(numberCouples) $(numberGames)

clean:
	rm -rf bin/**/*.class
	rm -rf bin/*.class
