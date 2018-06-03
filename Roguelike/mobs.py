from math import sqrt, pow
from fireball import *

MOB_SPEED = 4
HP = 1000

ICON_DIR = os.path.dirname(__file__)


#Класс моба
class Mob(Drawable):
    def __init__(self, x, y):
        Drawable.__init__(self, x, y)
        self.health = HP
        self.image = image.load("%s/images/dragon.png" % ICON_DIR)
        self.speed = MOB_SPEED
        self.fb = None
        self.slow_time = 0

    #Обновление информации о положении и харрактеристик
    def update(self, blocks, rect, level):
        left = right = up = down = 0
        if self.dist(rect.x, rect.y) < 600 and not self.dead():
            difX = rect.x - self.rect.x
            difY = rect.y - self.rect.y
            vecX = difX/max(abs(difX), abs(difY))
            vecY = difY / max(abs(difX), abs(difY))
            left = vecX < -0.5
            right = vecX > 0.5
            up = vecY < 0
            down = vecY > 0
            blockX, blockY = self.rect.x // 32, self.rect.y // 32
            cf = down - up
            if up or down:
                if level[blockY + cf][blockX] == "-":
                    levString = level[blockY + cf]
                    left, right = self.getDir(blockX, levString)
                else:
                    left = level[blockY + cf][blockX + 1] == "-" and right
                    right = not left
            else:
                self.fire(left)
        self.rec(left, right, up, down, blocks, self.speed)
        self.slow_time = (self.slow_time + 1) % 120
        if self.slow_time > 100:
            self.speed = MOB_SPEED

    #Определение направления движения
    def getDir(self, blockX, levString):
        leftSt = levString[:blockX]
        rightSt = levString[blockX + 1:]
        lnNearestLeft = len(leftSt.split(" ")[-1])
        lnNearestRight = len(rightSt.split(" ")[0])
        if lnNearestLeft < len(leftSt) and lnNearestRight < len(rightSt):
            left = lnNearestLeft < lnNearestRight
            right = not left
            return left, right
        else:
            left = lnNearestLeft < len(leftSt)
            right = not left
            return left, right

    #Взятие координат
    def getCoords(self):
        return self.rect.x, self.rect.y, self.xvel, self.yvel

    #Отрисовка
    def draw(self, screen, camera):
        if self.health > 0:
            screen.blit(self.image, camera.apply(self))
            if self.fb is not None:
                res = self.fb.update(self.block, [0, 0, 0])
                self.fb.draw(screen, camera)
                if not res:
                    self.fb = None

    #Функция расстояния
    def dist(self, x, y):
        return sqrt(pow(x - self.rect.x, 2) + pow(y - self.rect.y, 2))

    #Обработка попадания снаряда
    def engage(self, power, slow):
        self.health -= power
        self.speed = MOB_SPEED - slow
        self.slow_time = 0
        if self.health > 0:
            print("Dragon was damaged by " + str(power) + " health remain: " + str(self.health))
            if slow > 0:
                print("Dragon gain slow effect")
        else:
            print("Dragon dead")

    #Информация о смерти
    def dead(self):
        return self.health < 0

    #Выстрел
    def fire(self, left):
        if self.fb is None:
            self.fb = Fireball(self.rect.x, self.rect.y, int(2*(0.5 - left)), 0, 5, 200)

    #Обновление текущего блока с препятствиями и игроком
    def setBlock(self, block):
        self.block = block
