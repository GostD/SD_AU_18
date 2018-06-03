from block import *

WIDTH = 22
HEIGHT = 32
COLOR = "#888888"

"""Базовый класс для движущихся объектов"""
class Drawable(sprite.Sprite):
    def __init__(self, x, y):
        sprite.Sprite.__init__(self)
        self.xvel = 0
        self.yvel = 0
        self.startX = x
        self.startY = y
        self.image = Surface((WIDTH, HEIGHT))
        self.image.set_colorkey(Color(COLOR))
        self.rect = Rect(x, y, WIDTH, HEIGHT)

    """Отрисовка"""
    def draw(self, screen, camera):
        screen.blit(self.image, camera.apply(self))

    """Вычисление ускорения по нажатым клавишам"""
    def rec(self, left, right, up, down, blocks, speed):
        if left:
            self.xvel = -speed

        if right:
            self.xvel = speed

        if down:
            self.yvel = speed

        if up:
            self.yvel = -speed

        if not (left or right):
            self.xvel = 0

        if not (up or down):
            self.yvel = 0

        self.rect.y += self.yvel
        self.collide(0, self.yvel, blocks)

        self.rect.x += self.xvel
        self.collide(self.xvel, 0, blocks)

    """Обработка столкновений"""
    def collide(self, xvel, yvel, blocks):
        for p in blocks:
            if sprite.collide_rect(self, p) and not p.dead():
                if xvel > 0:
                    self.rect.right = p.rect.left
                if xvel < 0:
                    self.rect.left = p.rect.right
                if yvel > 0:
                    self.rect.bottom = p.rect.top
                if yvel < 0:
                    self.rect.top = p.rect.bottom

    """Информация о смерти"""
    def dead(self):
        return False