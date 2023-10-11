import svgwrite
import random
import lorem


# Create a function to generate a random SVG with specified elements
def generate_random_svg(filename):
    dwg = svgwrite.Drawing(filename, profile='tiny', size=('300mm', '300mm'))

    num_elements = random.randint(1, 10)

    for _ in range(num_elements):
        element_colour = random.choice(["red", "blue", "green", "yellow", "orange", "black", "pink"])
        element_type = random.choice(["circle", "rectangle", "text", "line", "curved_line"])
        opacity = random.uniform(0.3, 1.0)

        if element_type == "circle":
            cx = random.randint(20, 180)
            cy = random.randint(20, 180)
            r = random.randint(10, 90)
            dwg.add(dwg.circle(center=(cx, cy), r=r, fill=element_colour, opacity=opacity))
        elif element_type == "rectangle":
            x = random.randint(20, 160)
            y = random.randint(20, 160)
            width = random.randint(30, 100)
            height = random.randint(30, 100)
            dwg.add(dwg.rect(insert=(x, y), size=(width, height), fill=element_colour, opacity=opacity))
        elif element_type == "text":
            x = random.randint(20, 160)
            y = random.randint(20, 160)
            text = lorem.text().split()[:5]  # Limit to 5 words
            dwg.add(dwg.text(" ".join(text), insert=(x, y), fill="black", opacity=opacity))
        elif element_type == "line":
            x1 = random.randint(20, 180)
            y1 = random.randint(20, 180)
            x2 = random.randint(20, 180)
            y2 = random.randint(20, 180)
            dwg.add(dwg.line(start=(x1, y1), end=(x2, y2), stroke="black", stroke_width=2, opacity=opacity))
        elif element_type == "curved_line":
            x1 = random.randint(20, 180)
            y1 = random.randint(20, 180)
            x2 = random.randint(20, 180)
            y2 = random.randint(20, 180)
            x3 = random.randint(20, 180)
            y3 = random.randint(20, 180)
            dwg.add(dwg.path(d="M {} {} Q {} {} {} {}".format(x1, y1, x2, y2, x3, y3), stroke="black", fill="none", stroke_width=2, opacity=opacity))

        num_ellipses = random.randint(1, 10)
        if num_ellipses > 5:
            ellipse_cx = random.randint(20, 180)
            ellipse_cy = random.randint(20, 180)
            ellipse_rx = random.randint(10, 90)
            ellipse_ry = random.randint(10, 90)
            dwg.add(dwg.ellipse(center=(ellipse_cx, ellipse_cy), rx=ellipse_rx, ry=ellipse_ry, fill=element_colour, opacity=opacity))
    dwg.save()


# Generate three random SVG files
for i in range(60, 80):
    filename = f"random_svg_{i + 1}.svg"
    generate_random_svg(filename)
