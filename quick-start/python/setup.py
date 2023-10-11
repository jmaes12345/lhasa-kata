from setuptools import setup

# Basic project information
setup(
    name='lhasa-kata-quickstart',  # Choose a unique name for your project
    version='0.1',
    author='Your Name',
    description='Lhasa Kata Quick Start - Python',
    py_modules=['kata'],  # Specify the script as a module
    install_requires=[
        'beautifulsoup4',
        'xmltodict'
    ]
)
