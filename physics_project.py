import pygame
import math
pygame.init()

screen_width = 800
screen_height = 800
screen = pygame.display.set_mode((screen_width, screen_height))
cap = pygame.display.set_caption("Planets")


yellow = (255,255,0)
blue = (90,147,233)
red = (180,33,46)
Darker_Grey = (73,75,78)
white = (255,255,255)


class Planet:
    AU = 149.6e6 * 1000  # AU in meters
    G = 6.67428e-11
    SCALE = 250/AU # the scale is where 1AU = 100 pixels
    TIMESTEP = 3600*24


    def __init__(self,x,y,radius,color,mass):
        self.x = x
        self.y = y
        self.radius = radius
        self.color = color
        self.mass = mass
        self.x_velocity = 0
        self.y_velocity = 0
    
    def draw(self,window):
        x = self.x * self.SCALE + screen_width/2 
        y = self.y * self.SCALE + screen_height/2
        pygame.draw.circle(window,self.color,(x,y),self.radius)

    def forces(self,other_planet):
        distance_x = other_planet.x - self.x
        distance_y = other_planet.y - self.y
        distance = math.sqrt(distance_x**2 + distance_y**2)
        
        force = self.G * self.mass * other_planet.mass / distance**2
        theta = math.atan2(distance_y, distance_x)
        force_x = force * math.cos(theta)
        force_y = force * math.sin(theta)
        return force_x, force_y
    
    def position(self,other_planet):
        fx, fy = self.forces(other_planet)
        ax = fx / self.mass
        ay = fy / self.mass

        self.x_velocity += ax * self.TIMESTEP
        self.y_velocity += ay * self.TIMESTEP
        self.x += self.x_velocity * self.TIMESTEP
        self.y += self.y_velocity * self.TIMESTEP







        


   
    


    
    
     




running = True

sun = Planet(0,0,30,yellow,1.98892*(10**30))
earth = Planet(-1*Planet.AU,0,16,blue,5.982*(10**24))
earth.y_velocity = 29.783 * 1000
clock = pygame.time.Clock()






while running:
    clock.tick(60)
    screen.fill((0,0,0))
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    
    earth.position(sun)

    sun.draw(screen)
    earth.draw(screen)


    pygame.display.update()



    

