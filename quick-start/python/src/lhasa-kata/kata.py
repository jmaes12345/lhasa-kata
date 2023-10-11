import os
import sys
import shutil
import xmltodict
from bs4 import BeautifulSoup

# If no argument is provided, default to './svgs'
base_dir = sys.argv[1] if len(sys.argv) > 1 else './test-data'


# Parse SVG file into a dictionary
def parse_svg_xmltodict(file_path):
    with open(file_path, 'r') as file:
        return xmltodict.parse(file.read())


def parse_svg_bs4(file_path):
    with open(file_path, 'r') as file:
        return BeautifulSoup(file, 'xml')


# Determine the category based on SVG content
def determine_category(file_path):
    # use the following variable if you prefer xmltodict
    svg_dict = parse_svg_xmltodict(file_path)
    # use the following variable if you prefer Beautiful Soup
    soup = parse_svg_bs4(file_path)
    # TODO add rules here
    return -1


# Copy the SVG file to its respective sub-folder based on its category
def copy_file_to_sub_folder(file, category):
    old_path = os.path.join(base_dir, "input", file)
    new_path = os.path.join(base_dir, category, file)

    # Create the sub-folder if it doesn't exist
    os.makedirs(os.path.join(base_dir, category), exist_ok=True)

    # Copy the file
    shutil.copy2(old_path, new_path)
    print(f"Copied {file} to {category} folder.")


def process_svg_files():
    for file in os.listdir(base_dir):
        if file.endswith('.svg'):
            category = determine_category(os.path.join(base_dir, file))
            category_str = ""
            # Convert category number to string
            switcher = {
                1: "Cat1",
                2: "Cat2",
                3: "Cat3"
            }
            category_str = switcher.get(category, "Unclassified")
            copy_file_to_sub_folder(file, category_str)


if __name__ == "__main__":
    process_svg_files()
