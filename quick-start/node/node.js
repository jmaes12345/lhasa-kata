const fs = require('fs');
const path = require('path');
const xml2js = require('xml2js');

const baseDir = './test-data'; // Directory containing the SVG files
const inputDir = path.join(baseDir,'input'); // Directory containing the SVG files

// Read all SVG files from the base directory into an array
const readSVGFiles = () => {
    return fs.readdirSync(inputDir).filter(file => path.extname(file) === '.svg');
};

// Parse SVG file into a JavaScript object
const parseSVG = async (file) => {
    let f = path.join(inputDir, file);
    const data = await fs.promises.readFile(f, { encoding: 'utf-8' });
    return xml2js.parseStringPromise(data);
};

// Dummy function to determine the category of an SVG file
// You can replace this with your own logic
const determineCategory = async (file) => {
    // ... add conditions as needed
    const svgObj = await parseSVG(file);
    
    //Add rules to determine category

    return -1;
};

// Move the SVG file to its respective sub-folder based on its category
const copyFileToSubFolder = async (file, category) => {
    const oldPath = path.join(baseDir,'input', file);

    //default folder name to unclassified
    let catfolder = 'Unclassified';

    //translate category to folder name
    switch (category) {
        case -1: catfolder = 'Unclassified'; break;
        case 1: catfolder = 'Cat1'; break;
        case 2: catfolder = 'Cat2'; break;
        case 3: catfolder = 'Cat3'; break;
    }

    //specify new path dynamically
    const newPath = path.join(baseDir,'output', catfolder, file);

    // Create the sub-folder if it doesn't exist
    if (!fs.existsSync(path.join(baseDir, 'output', catfolder))) {
        fs.mkdirSync(path.join(baseDir, 'output', catfolder));
    }

    // Copy the file
    fs.copyFile(oldPath, newPath, (err) => {
        if (err) throw err;
        console.log(`Copied ${file} to ${catfolder} folder.`);
    });
};

// Main function to process each SVG file
const processSVGFiles = async () => {
    const svgFiles = readSVGFiles();
    svgFiles.forEach(async file => {
        const category = await determineCategory(file);
        copyFileToSubFolder(file, category);
    });
};

// Run the main function
processSVGFiles().catch(err => console.error(err));
