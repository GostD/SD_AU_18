from block import *
import random
from drawable import *


FIREBALL_SPEED = 14
FIREBALL_POWER = 300

ICON_DIR = os.path.dirname(__file__)

#Класс фаербола
class Fireball(Drawable):
    def __init__(self, x, y, xvel, yvel, spd=0, decrease=0):
        Drawable.__init__(self, x, y)
        self.spd = spd
        self.decrease = decrease
        self.speed = FIREBALL_SPEED - spd
        self.power = FIREBALL_POWER - decrease
        self.slow = 0
        if xvel == 0 and yvel == 0:
            rnd = random.randint(0, 1)
            rndDir = random.randint(0, 1)
            if rndDir:
                xvel = int(2*(rnd-0.5))*self.speed
            else:
                yvel = int(2 * (rnd - 0.5))*self.speed
        self.velocity(xvel, yvel)
        self.image = image.load("%s/images/bolt1.png" % ICON_DIR)

    #Вычисление
    def velocity(self, xvel, yvel):
        self.xvel = (xvel > 0) * self.speed - (xvel < 0) * self.speed
        self.yvel = (yvel > 0) * self.speed - (yvel < 0) * self.speed

    #Обновление координат силы и ускорения
    def update(self, blocks, bolt):
        self.power = FIREBALL_POWER - self.decrease + bolt[0]
        self.speed = FIREBALL_SPEED - self.spd + bolt[1]
        self.slow = bolt[2]
        self.velocity(self.xvel, self.yvel)

        self.rect.y += self.yvel
        bangX = self.collide(0, self.yvel, blocks)

        self.rect.x += self.xvel
        bangY = self.collide(self.xvel, 0, blocks)
        return bangX and bangY

    #Обработка столкновений
    def collide(self, xvel, yvel, blocks):
        for p in blocks:
            if sprite.collide_rect(self, p) and not p.dead():
                p.engage(self.power, self.slow)
                self.image = image.load("%s/images/cloud_fire1.png" % ICON_DIR)
                if xvel > 0:
                    self.rect.right = p.rect.left
                if xvel < 0:
                    self.rect.left = p.rect.right
                if yvel > 0:
                    self.rect.bottom = p.rect.top
                if yvel < 0:
                    self.rect.top = p.rect.bottom
                return False
        return True
