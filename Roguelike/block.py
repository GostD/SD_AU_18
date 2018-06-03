from pygame import *
import os

BLOCK_WIDTH = 32
BLOCK_HEIGHT = 32
BLOCK_COLOR = "#8B0000"
ICON_DIR = os.path.dirname(__file__)

#Класс неподвижного блока
class Block(sprite.Sprite):
    def __init__(self, x, y):
        sprite.Sprite.__init__(self)
        self.image = Surface((BLOCK_WIDTH, BLOCK_HEIGHT))
        self.image = image.load("%s/images/brick_dark0.png" % ICON_DIR)
        self.rect = Rect(x, y, BLOCK_WIDTH, BLOCK_HEIGHT)

    #Отрисовка блока
    def draw(self, screen, camera):
        screen.blit(self.image, camera.apply(self))

    #Пусто
    def engage(self, power, slow):
        pass

    #Пусто
    def dead(self):
        return False