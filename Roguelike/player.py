from drawable import *

MOVE_SPEED = 7
HEALTH = 1000

ICON_DIR = os.path.dirname(__file__)


#Класс игрока
class Player(Drawable):
    def __init__(self, x, y):
        Drawable.__init__(self, x, y)
        self.image = image.load("%s/images/ijyb.png" % ICON_DIR)
        self.health = HEALTH

    #Обновление информации об игроке
    def update(self, left, right, up, down, blocks, neck):
        if self.health < HEALTH:
            self.health += neck[0]
        self.rec(left, right, up, down, blocks, MOVE_SPEED + neck[1])

    #Взятие координат игрока
    def getCoords(self):
        return self.rect.x, self.rect.y, self.xvel, self.yvel

    #Информация о смерти
    def dead(self):
        return self.health < 0

    #Поправка на жизни при попадании снаряда
    def engage(self, power, slow):
        self.health -= power
        if self.health > 0:
            print("You were damaged on " + str(100) + " heat points, health remain: " + str(self.health))
        if self.health < 0:
            print("You Dead")

    #Взятие здоровья
    def getHealth(self):
        return str(self.health)
