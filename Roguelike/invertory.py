from pygame import *
import os

INV_WIDTH = 32
INV_HEIGHT = 32
WIN_WIDTH = 800
WIN_HEIGHT = 640
INV_COLOR = "#8B0000"
COLOR = "#888888"
ICON_DIR = os.path.dirname(__file__)
EFFECTS = [[
                [2, 0, 0],
                [1, 4, 0],
                [1, 0, 2]
            ],
            [
                [1, 2],
                [2, 0],
                [0, 4],
            ]]


"""Класс для инвертаря"""
class Inventory(sprite.Sprite):
    """Класс ячейки инвертаря"""
    class Module(sprite.Sprite):
        def __init__(self, x, y):
            sprite.Sprite.__init__(self)
            self.image = Surface((INV_WIDTH, INV_HEIGHT))
            self.img = "sandstone_floor0.png"
            self.type = "empty"
            if y < 3:
                if x == 0:
                    self.img = "bolt" + str(y) + ".png"
                    self.type = "bolt"
                    self.effects = EFFECTS[0][y]
                if x == 2:
                    self.img = "crystal" + str(y) + ".png"
                    self.type = "crystal"
                    self.effects = EFFECTS[1][y]
            self.image = image.load(("%s/images/" + self.img) % ICON_DIR)
            self.rect = Rect(INV_WIDTH*x, INV_HEIGHT*y, INV_WIDTH, INV_HEIGHT)
            self.xBase = INV_WIDTH*x
            self.yBase = INV_HEIGHT*y

        """Отрисовка ячейки"""
        def draw(self, screen):
            screen.blit(self.image, (self.xBase, self.yBase))

        """Возвращение информации о предмете в ячейке"""
        def getType(self):
            return self.img, self.type, self.effects

        """Изменение содержимого ячейки"""
        def swapImg(self, img, effects):
            self.image = image.load(("%s/images/" + img) % ICON_DIR)
            self.effects = effects

    def __init__(self):
        sprite.Sprite.__init__(self)
        self.invBlocks = ["   ",
                          "   ",
                          "   "]
        self.modules = []
        self.curPointer = [0, 0]

        self.neckage_block = Inventory.Module(0, (WIN_HEIGHT - INV_HEIGHT)//INV_HEIGHT)
        self.neckage_block.effects = [0, 0]
        self.fireball_block = Inventory.Module(1, (WIN_HEIGHT - INV_HEIGHT)//INV_HEIGHT)
        self.fireball_block.effects = [0, 0, 0]

        self.pointer = Surface((INV_WIDTH, INV_HEIGHT))
        self.pointer = image.load("%s/images/cursor_green.png" % ICON_DIR)
        self.pointer.set_colorkey(Color(COLOR))
        self.rect = Rect(self.curPointer[0], self.curPointer[1], INV_WIDTH, INV_HEIGHT)

        for i in range(len(self.invBlocks)):
            for j in range(len(self.invBlocks[0])):
                self.modules.append(Inventory.Module(j, i))
        self.modules.append(self.neckage_block)
        self.modules.append(self.fireball_block)

    """Изменения во время просмотра инвертаря"""
    def update(self, left, right, up, down, space):
        if space:
            mod = self.modules[self.curPointer[1]*3 + self.curPointer[0]]
            img, type, effects = mod.getType()
            if type == "crystal":
                self.neckage_block.swapImg(img, effects)

            elif type == "bolt":
                self.fireball_block.swapImg(img, effects)

        else:
            if left:
                self.curPointer[0] = max(0, self.curPointer[0] - 1)
            if right:
                self.curPointer[0] = min(2, self.curPointer[0] + 1)
            if up:
                self.curPointer[1] = max(0, self.curPointer[1] - 1)
            if down:
                self.curPointer[1] = min(2, self.curPointer[1] + 1)

    """Отрисовка инвентаря"""
    def draw(self, screen):
        for i in self.modules:
            i.draw(screen)
        screen.blit(self.pointer, (self.curPointer[0]*INV_WIDTH, self.curPointer[1]*INV_HEIGHT))

    """Возвращение эффектов подвески и фаербола"""
    def getEffects(self):
        return self.neckage_block.effects, self.fireball_block.effects
