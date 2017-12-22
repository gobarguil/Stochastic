k <- 1:10
turnsRequired = log(factorial(k))
turnsRequired = turnsRequired/(log(k + 1) + 1)
p = plot(k, turnsRequired, col="blue",
     main="Number of turns required to finish the game ideally", xlab="number of couples", ylab="number of turns")
lines(k, turnsRequired)