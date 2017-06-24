### Grundlagen der Bioinformatik
### Übung 4
### Gruppe 11

### Aufgabe 1
klausur <-
  c(3.3, 1.7, 2.0, 4.0, 1.3, 2.0, 3.0, 2.7, 3.7, 2.3, 1.7, 2.3)

mean(klausur)
# [1] 2.5

sd(klausur)
# [1] 0.8485281

median(klausur)
# [1] 2.3

sum(klausur) / length(klausur)
# [1] 2.5


### Aufgabe 2
png(file = "2a.png")
hist(
  klausur,
  main = "Klausurergebnisse",
  col = "orange",
  xlab = "Note",
  ylab = "Anzahl"
)
dev.off()

png(file = "2b.png")
boxplot(klausur,
        main = "Klausurergebnisse",
        col = "lightblue",
        ylab = "Note")
dev.off()


### Aufgabe 3
data("faithful")
sd(faithful$eruptions)
# [1] 1.141371

sd(faithful$waiting)
# [1] 13.59497

mean(faithful$eruptions)
# [1] 3.487783

mean(faithful$waiting)
# [1] 70.89706

cor(faithful$eruptions, faithful$waiting)
# [1] 0.9008112

png(file = "3d.png")
plot(faithful, main = "Yellowstone eruptions and waiting", pch = 20)
dev.off()


### Aufgabe 4
x <- c(3, 7, 1, 10, 15, 8, 11, 2, 12)
y <- c(8, 6, 2, 0, 4, 11, 9, 17, 3)
z <- c(x[-9], y[-9])
#  [1]  3  7  1 10 15  8 11  2  8  6  2  0  4 11  9 17

z[z > 9] <- 9
#  [1] 3 7 1 9 9 8 9 2 8 6 2 0 4 9 9 9


### Aufgabe 5
library(MASS)
data("anorexia")
mean(anorexia$Postwt[anorexia$Prewt < anorexia$Postwt])
# [1] 90.09048

png(file = "5c.png")
plot(anorexia$Postwt, anorexia$Prewt, pch = c(1, 4, 16)[as.numeric(anorexia$Treat)], xlim = c(70, 105), ylim = c(70, 105), main="Anorexia Therapie", xlab="Gewicht nach der Therapie", ylab="Gewicht vor der Therapie")
legend("topright", inset = .05, legend = c("Cont", "CBT", "FT"), pch = c(4, 1, 16))
dev.off()


### Aufgabe 6
data("airquality")
weather <-
  data.frame(airquality$Temp, as.Date(paste(airquality$Day, airquality$Month, sep = "."), "%d.%m"))
names(weather) <- c("Temp", "Datum")
png(file = "6a.png", width = 800)
plot(
  weather$Datum,
  weather$Temp,
  main = "Temperaturverlauf",
  type = "n",
  xlab = "Datum",
  ylab = "Temperatur in Grad Fahrenheit",
  ylim = c(50, 100)
)
lines(weather$Datum, weather$Temp, col = "green")
dev.off()

png(file = "6b.png", width = 800)
plot(
  weather$Datum,
  weather$Temp,
  main = "Temeraturverlauf mit Glättungskurve",
  type = "n",
  xlab = "Datum",
  ylab = "Temperatur in Grad Fahrenheit",
  ylim = c(50, 100)
)
lines(weather$Datum, weather$Temp, col = "green")
smoothedTemp <- smooth.spline(weather$Temp, spar = 0.5)
weather$Temp <- smoothedTemp$y
lines(weather$Datum, weather$Temp, col = "red")
dev.off()


### Aufgabe 7
a <- sapply(1:80, function(x)
  mean(rnorm(10, 50, 15)))
b <- sapply(1:80, function(x)
  mean(rnorm(100, 50, 15)))
c <- sapply(1:80, function(x)
  mean(rnorm(1000, 50, 15)))
mean(a)
# [1] 50.42338

mean(b)
# [1] 49.92031

mean(c)
# [1] 50.03936

png(file = "7.png")
boxplot(
  a,
  b,
  c,
  names = c("10", "100", "1000"),
  col = c("yellow", "lightgreen", "lightblue"),
  ylab = "Werte",
  xlab = "Anzahl Stichproben"
)
dev.off()


### Aufgabe 8
vorher <- c(34, 56, 45, 47, 69, 93, 51, 63, 54, 62)
nachher <- c(31, 55, 47, 44, 73, 89, 44, 60, 50, 61)
var.test(vorher, nachher, ratio =1, alternative = c("two.sided")) 
#F-Test, p-value = 0.9375 >>0.01 => Varianzen von vorher und nachher sind gleich =>var.equal=TRUE
# gleiche Versuchspersonen => paired =TRUE
t.test(vorher,nachher,alternative=c("two.sided"),paired=TRUE,var.equal=TRUE)
#t-test, p-value =  0.07655 > 0.01 => nicht signifikant
# das heisst, die Testleistungen vorher/nachher weichen nur zufällig voneinander ab

### Aufgabe 9
tapply(airquality$Temp, airquality$Month, mean)
#        5        6        7        8        9
# 65.54839 79.10000 83.90323 83.96774 76.90000

tapply(airquality$Ozone, airquality$Month, mean, na.rm = TRUE)
#        5        6        7        8        9
# 23.61538 29.44444 59.11538 59.96154 31.44828


### Aufgabe 10
data("Orange")
wachstum <-
  apply(data.frame(Orange$circumference, Orange$age), 1, function(x)
    x[1] / x[2])
#  [1] 0.25423729 0.11983471 0.13102410 0.11454183 0.09748172 0.10349854
#  [7] 0.09165613 0.27966102 0.14256198 0.16716867 0.15537849 0.13972380
# [13] 0.14795918 0.12831858 0.25423729 0.10537190 0.11295181 0.10756972
# [19] 0.09341998 0.10131195 0.08849558 0.27118644 0.12809917 0.16867470
# [25] 0.16633466 0.14541024 0.15233236 0.13527181 0.25423729 0.10123967
# [31] 0.12198795 0.12450199 0.11535337 0.12682216 0.11188369

max(wachstum)
# [1] 0.279661

min(wachstum)
# [1] 0.08849558


### Aufgabe 11
iterativ <- function(n) {
  a <- 0
  for (var in 1:n) {
    a <- a + var
  }
  return(a)
}

rekursiv <- function(n) {
  ifelse(n < 1, return(n), return(n + rekursiv(n - 1)))
}