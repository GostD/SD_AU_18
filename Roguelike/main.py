import pygame
from pygame import *
from player import *
from mobs import *
from camera import *
from invertory import *

WIN_WIDTH = 800
WIN_HEIGHT = 640
DISPLAY = (WIN_WIDTH, WIN_HEIGHT)
BACKGROUND_COLOR = "#808080"


entities = pygame.sprite.Group()
blocks = []
blockWithEnemies = []
enemies = []
blocksWithHero = []

level = [
       "----------------------------------",
       "-s                               -",
       "------------    ---------------- -",
       "-                                -",
       "- ---------------------     ------",
       "-    +                           -",
       "-------------------------------- -",
       "-                      +         -",
       "- ------       -------------------",
       "-                                -",
       "-----      ---------    -------- -",
       "-                                -",
       "- -----      ----------  ---------",
       "- +                 +            -",
       "----      --------------------   -",
       "-                                -",
       "- ------------          ----------",
       "-                                -",
       "----           ----------------- -",
       "-                                -",
       "- ------------------      --------",
       "-                           +    -",
       "-     +                          -",
       "----------------------------------"]


def main():
    pygame.init()
    assert pygame is not None
    screen = pygame.display.set_mode(DISPLAY)
    pygame.display.set_caption("Roguelike")
    bg = Surface((WIN_WIDTH, WIN_HEIGHT))
    bg.fill(Color(BACKGROUND_COLOR))

    hero = None
    timer = pygame.time.Clock()

    left = right = up = down = space = False

    x = y = 0
    for row in level:
        for col in row:
            if col == "-":
                pf = Block(x, y)
                entities.add(pf)
                blocks.append(pf)

            elif col == "+":
                mob = Mob(x, y)
                assert mob is not None
                entities.add(mob)
                enemies.append(mob)
                blockWithEnemies.append(mob)

            elif col == "s":
                hero = Player(x, y)

            x += BLOCK_WIDTH
        y += BLOCK_HEIGHT
        x = 0

    assert hero is not None

    entities.add(hero)

    for b in blocks:
        blockWithEnemies.append(b)
        blocksWithHero.append(b)
    blocksWithHero.append(hero)

    for mob in enemies:
        mob.setBlock(blocksWithHero)

    total_level_width = len(level[0]) * BLOCK_WIDTH
    total_level_height = len(level) * BLOCK_HEIGHT
    drawFb = False
    fb = None
    inv = False
    mobs_alive = True
    inven = Inventory()

    camera = Camera(camera_configure, total_level_width, total_level_height)


    while 1:
        timer.tick(60)
        for e in pygame.event.get():
            if e.type == QUIT:
                raise SystemExit("QUIT")
            if e.type == KEYDOWN and e.key == K_LEFT:
                left = True
            if e.type == KEYDOWN and e.key == K_RIGHT:
                right = True
            if e.type == KEYDOWN and e.key == K_UP:
                up = True
            if e.type == KEYDOWN and e.key == K_DOWN:
                down = True
            if e.type == KEYDOWN and e.key == K_SPACE:
                space = True

            if e.type == KEYUP and e.key == K_LEFT:
                left = False
            if e.type == KEYUP and e.key == K_RIGHT:
                right = False
            if e.type == KEYUP and e.key == K_UP:
                up = False
            if e.type == KEYUP and e.key == K_DOWN:
                down = False
            if e.type == KEYUP and e.key == K_SPACE:
                space = False

            if e.type == KEYDOWN and e.key == K_i:
                inv = not inv

        font = pygame.font.Font(None, 25)

        screen.blit(bg, (0, 0))
        if inv:
            for e in entities:
                e.draw(screen, camera)
            inven.update(left, right, up, down, space)
            time.wait(80)
            inven.draw(screen)
        else:
            neck, bolt = inven.getEffects()
            hero.update(left, right, up, down, blocks, neck)
            assert hero is not None
            if hero.dead() or not mobs_alive:
                txt = "YOU WIN!"
                if not mobs_alive:
                    "YOU_LOSE!"
                text = font.render(txt, True, (255, 0, 0))
                screen.blit(text, [360, 300])
                pygame.display.update()
                time.wait(2000)
                raise SystemExit("QUIT")
            any_alive = False
            for mob in enemies:
                mob.update(blocks, hero.rect, level)
                assert mob is not None
                any_alive = any_alive or not mob.dead()
            mobs_alive = any_alive
            if space and not drawFb:
                drawFb = True
                hX, hY, hXvel, hYvel = hero.getCoords()
                fb = Fireball(hX, hY, hXvel, hYvel)
                assert fb is not None
                fb.draw(screen, camera)
            elif drawFb:
                assert fb is not None
                drawFb = fb.update(blockWithEnemies, bolt)
                fb.draw(screen, camera)

            camera.update(hero)
            for e in entities:
                e.draw(screen, camera)

        text = font.render(hero.getHealth(), True, (255, 0, 0))
        screen.blit(text, [760, 16])
        pygame.display.update()


if __name__ == "__main__":
    main()
