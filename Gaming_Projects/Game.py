import pygame
import random
import math
pygame.init()


screen_width = 800
screen_height = 600
screen = pygame.display.set_mode((screen_width, screen_height))


pygame.display.set_caption('Manas Game')

game_icon = pygame.image.load('shield.png')
pygame.display.set_icon(game_icon)

background_img = pygame.image.load('background.png')


score = 0
text_type = pygame.font.Font('freesansbold.ttf',32)
text_x = 10
text_y = 10

def display_score(x,y):
    value = text_type.render("Score : " + str(score),True,(255,255,255))
    screen.blit(value,(x,y))


game_over_text = pygame.font.Font('freesansbold.ttf',32)
game_over_x = 308
game_over_y = 270
def game_over(x,y):
    display = game_over_text.render("GAME OVER ",True,(255,255,255))
    screen.blit(display,(x,y))






hero_img = pygame.image.load('superhero.png')
hero_x = 370
hero_y = 500
herox_change = 0

enemy_img = []
enemy_x = []
enemy_y = []
enemy_x_change = []
enemy_y_change = []
num_enemies = 5

for i in range(0,num_enemies):
    enemy_img.append(pygame.image.load('enemy.png'))
    enemy_x.append(random.randint(0,735))
    enemy_y.append(random.randint(15,150))
    enemy_x_change.append(3)
    enemy_y_change.append(20)


bullet_img = pygame.image.load('bullet.png')
bullet_x = 0
bullet_y = 500
bullet_x_change = 0
bullet_y_change = 7
bullet_state = "ready"

def hero(x,y):
    screen.blit(hero_img,(x,y))

def enemy(x,y,i):
    screen.blit(enemy_img[i],(x,y))

def bullet_shot(x,y):
    screen.blit(bullet_img,(x,y))

def hit(enemy_x,enemy_y,bullet_x,bullet_y):
    distance = math.sqrt((enemy_x-bullet_x)**2 + (enemy_y-bullet_y)**2)
    if distance < 35:
        return True
    else:
        return False

running = True
while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        if event.type == pygame.KEYDOWN:
            if event.key == pygame.K_LEFT:
                herox_change = -4
            if event.key == pygame.K_RIGHT:
                herox_change = 4
            if event.key == pygame.K_SPACE:
                if bullet_state == "ready":
                    bullet_x = hero_x
                    bullet_state = 'fire'
        if event.type == pygame.KEYUP:
            if event.key == pygame.K_LEFT or event.key == pygame.K_RIGHT:
                herox_change = 0
    
         




    #screen.fill((0,255,255)) # AQUA BLUE R,G,B colors
    screen.fill((0,0,0))
    screen.blit(background_img,(0,0))
    
 

    hero_x = hero_x + herox_change
    if hero_x <= 0:
        
        hero_x = 0
    elif hero_x >= 736:
        hero_x = 736
    
    

    for i in range(0,num_enemies):

        if enemy_y[i] > 440:
            for j in range(0,num_enemies):
                enemy_y[j] = 2000
            game_over(game_over_x,game_over_y)
            break

        enemy_x[i] = enemy_x[i] + enemy_x_change[i]
     
        if enemy_x[i] <= 0:
            
            enemy_x_change[i] = 3
            enemy_y[i] = enemy_y[i]+enemy_y_change[i]
        elif enemy_x[i] >= 736:
            enemy_x_change[i] = -3
            enemy_y[i] = enemy_y[i] + enemy_y_change[i]

        if hit(enemy_x[i],enemy_y[i],bullet_x,bullet_y):
            bullet_y = 500
            bullet_state ="ready"
            score = score + 1
            
            enemy_x[i] = random.randint(0,735)
            enemy_y[i] = random.randint(15,150)
        enemy(enemy_x[i],enemy_y[i],i)

    if bullet_y <= 0:
        bullet_y = 500
        bullet_state ="ready"

    if bullet_state is "fire":
        bullet_shot(bullet_x,bullet_y)
        bullet_y = bullet_y - bullet_y_change
    


    hero(hero_x,hero_y)
    display_score(text_x,text_y)
    pygame.display.update()

    




